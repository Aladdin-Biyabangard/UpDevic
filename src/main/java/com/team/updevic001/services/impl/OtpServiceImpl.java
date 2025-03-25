package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Otp;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.repositories.OtpRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.mail.ConfirmationEmailServiceImpl;
import com.team.updevic001.mail.EmailTemplate;
import com.team.updevic001.model.dtos.request.security.OtpRequest;
import com.team.updevic001.services.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {
    OtpRepository otpRepository;
    ConfirmationEmailServiceImpl emailService;

    @Override
    public void sendOtp(User user) {
        log.info("Operation of sending otp started for user with email {}", user.getEmail());
        String code = generateOtp(user.getEmail());
        Map<String, String> placeholders = Map.of("userName", user.getFirstName(), "code", code);
        emailService.sendEmail(user.getEmail(), EmailTemplate.VERIFICATION, placeholders);
        log.info("Otp code sent to user email {}", user.getEmail());
    }


    @Override
    public void verifyOtp(OtpRequest otpRequest) {
        Otp otp = otpRepository.findByCodeAndEmail(otpRequest.getOtpCode(), otpRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("OTP_NOT_FOUND"));
        if (otp.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.error("Otp is expired for user with email {}", otpRequest.getEmail());
            throw new IllegalArgumentException("OTP_EXPIRED");
        }
    }

    private String generateOtp(String email) {
        log.info("Operation of generating otp started for user {}", email);
        SecureRandom random = new SecureRandom();
        Integer code = 100_000 + random.nextInt(900_000);
        Otp otp = Otp.builder()
                .email(email)
                .code(code)
                .expirationTime(LocalDateTime.now().plusMinutes(15))
                .build();
        otpRepository.save(otp);
        log.info("Otp generated for user with email {}", email);
        return String.valueOf(code);
    }
}
