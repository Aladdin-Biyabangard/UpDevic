package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ExpiredRefreshTokenException;
import com.team.updevic001.exceptions.ResourceAlreadyExistException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.exceptions.UnauthorizedException;
import com.team.updevic001.mail.ConfirmationEmailServiceImpl;
import com.team.updevic001.mail.EmailTemplate;
import com.team.updevic001.model.dtos.request.security.*;
import com.team.updevic001.model.dtos.response.AuthResponseDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.interfaces.AuthService;
import com.team.updevic001.services.interfaces.OtpService;
import com.team.updevic001.utility.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    UserRoleRepository userRoleRepository;
    UserProfileRepository userProfileRepository;
    OtpService otpService;
    StudentRepository studentRepository;
    ConfirmationEmailServiceImpl confirmationEmailServiceImpl;
    PasswordResetTokenRepository passwordResetTokenRepository;
    RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public AuthResponseDto login(AuthRequestDto authRequest) {
        log.info("Attempting to authenticate user with email: {}", authRequest.getEmail());

        User user = authenticateUser(authRequest);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            log.info("Authentication successful for user with email: {}", authRequest.getEmail());

            return getAccessTokenAndRefreshToken(user);

        } catch (BadCredentialsException ex) {
            log.warn("Invalid login attempt for email: {}", authRequest.getEmail());
            throw new UnauthorizedException("Invalid email or password.");
        }
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        log.info("Registration process for user is started.");
        validateUserRequest(request);
        UserRole userRole = assignDefaultRole();

        Student student = createUser(request, userRole);
        UserProfile userProfile = UserProfile.builder()
                .user(student)
                .build();

        studentRepository.save(student);
        userProfileRepository.save(userProfile);
        log.info("New user with email {} saved with status {}", student.getEmail(), student.getStatus());
        otpService.sendOtp(student);
    }

    @Transactional
    public AuthResponseDto getAccessTokenAndRefreshToken(User user) {
        var jwtToken = jwtUtil.createToken(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);
        AuthResponseDto authResponse = AuthResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getUuid())
                .build();
        log.info("Access token and refresh token are returned");
        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponseDto verifyAndGetToken(OtpRequest request) {
        log.info("Operation of verifying and generating token started");
        User user = userRepository.findByEmailAndStatus(request.getEmail(), Status.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
        otpService.verifyOtp(request);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return getAccessTokenAndRefreshToken(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
        log.info("Requesting password reset started by user with ID {}", user.getId());
        String token = generateToken();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expirationTime(LocalDateTime.now().plusMinutes(15))
                .build();
        passwordResetTokenRepository.save(passwordResetToken);
        Map<String, String> placeholders = Map.of("userName", user.getFirstName(), "link", token);
        confirmationEmailServiceImpl.sendEmail(email, EmailTemplate.PASSWORD_RESET, placeholders);
    }

    @Override
    @Transactional
    public void resetPassword(String token, RecoveryPassword recoveryPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("SUCH_TOKEN_NOT_FOUND"));
        User user = resetToken.getUser();
        if (isExpired(resetToken)) {
            throw new IllegalArgumentException("TOKEN_EXPIRED");
        }
        if (!recoveryPassword.getNewPassword().equals(recoveryPassword.getRetryPassword())) {
            throw new IllegalArgumentException("PASSWORDS_MISMATCHING");
        }
        user.setPassword(passwordEncoder.encode(recoveryPassword.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
        log.info("Password was successfully recovered");
    }

    @Override
    public AuthResponseDto refreshAccessToken(RefreshTokenRequest tokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findByUuidAndExpiresAtAfter(tokenRequest.getUuid(), LocalDateTime.now())
                .orElseThrow(() -> new ExpiredRefreshTokenException("REFRESH_TOKEN_EXPIRED_OR_INVALID"));
        User user = refreshToken.getUser();

        String newAccessToken = jwtUtil.createToken(user);

        return new AuthResponseDto(newAccessToken, tokenRequest.getUuid());
    }

    private boolean isExpired(PasswordResetToken resetToken) {
        return resetToken.getExpirationTime().isBefore(LocalDateTime.now());
    }


    private void validateUserRequest(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("User with email {} already exist.", request.getEmail());
            throw new ResourceAlreadyExistException("USER_ALREADY_EXISTS");
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            log.error("Password and confirm password mismatches");
            throw new IllegalArgumentException("PASSWORD_MISMATCHING");
        }
    }

    private Student createUser(RegisterRequest request, UserRole userRole) {
        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .status(Status.PENDING)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .build();

    }

    private UserRole assignDefaultRole() {
        return userRoleRepository.findByName(Role.STUDENT).orElseGet(() -> {
            UserRole userRole = new UserRole(null, Role.STUDENT);
            return userRoleRepository.save(userRole);
        });
    }

    private User authenticateUser(AuthRequestDto authRequest) {
        return userRepository.findByEmailAndStatus(authRequest.getEmail(), Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND_OR_INACTIVE"));
    }

    private static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}
