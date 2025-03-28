package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.StudentRepository;
import com.team.updevic001.dao.repositories.UserProfileRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.dao.repositories.UserRoleRepository;
import com.team.updevic001.exceptions.ResourceAlreadyExistException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.exceptions.UnauthorizedException;
import com.team.updevic001.model.dtos.request.security.AuthRequestDto;
import com.team.updevic001.model.dtos.request.security.OtpRequest;
import com.team.updevic001.model.dtos.request.security.RegisterRequest;
import com.team.updevic001.model.dtos.response.AuthResponseDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.interfaces.AuthService;
import com.team.updevic001.services.interfaces.OtpService;
import com.team.updevic001.utility.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
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

            String jwtToken = jwtUtil.createToken(user);
            log.info("Authentication successful for user with email: {}", authRequest.getEmail());

            return AuthResponseDto.builder()
                    .accessToken(jwtToken)
                    .build();

        } catch (BadCredentialsException ex) {
            log.warn("Invalid login attempt for email: {}", authRequest.getEmail());
            throw new UnauthorizedException("Invalid email or password.");
        }
    }


    @Override
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

    @Override
    public AuthResponseDto verifyAndGetToken(OtpRequest request) {
        log.info("Operation of verifying and generating token started");
        User user = userRepository.findByEmailAndStatus(request.getEmail(), Status.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));
        otpService.verifyOtp(request);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        var jwtToken = jwtUtil.createToken(user);
        return AuthResponseDto.builder()
                .accessToken(jwtToken)
                .build();
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
        return userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}