package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.security.RecoveryPassword;
import com.team.updevic001.model.dtos.request.security.AuthRequestDto;
import com.team.updevic001.model.dtos.request.security.OtpRequest;
import com.team.updevic001.model.dtos.request.security.RefreshTokenRequest;
import com.team.updevic001.model.dtos.request.security.RegisterRequest;
import com.team.updevic001.model.dtos.response.AuthResponseDto;
import com.team.updevic001.services.interfaces.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    private final AuthService authServiceImpl;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        authServiceImpl.register(request);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(authServiceImpl.verifyAndGetToken(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest) {
        AuthResponseDto response = authServiceImpl.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestPasswordReset(@RequestParam @NotBlank String email) {
        authServiceImpl.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset token has been sent to user's email");
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam @NotBlank String token, @Valid @RequestBody RecoveryPassword recoveryPassword) {
        authServiceImpl.resetPassword(token, recoveryPassword);
        return ResponseEntity.ok("Password has been successfully reset.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequest request) {
        return new ResponseEntity<>(authServiceImpl.refreshAccessToken(request), HttpStatus.OK);
    }

}