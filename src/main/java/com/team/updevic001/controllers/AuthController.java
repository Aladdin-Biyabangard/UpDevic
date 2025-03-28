package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.security.AuthRequestDto;
import com.team.updevic001.model.dtos.request.security.OtpRequest;
import com.team.updevic001.model.dtos.request.security.RegisterRequest;
import com.team.updevic001.model.dtos.response.AuthResponseDto;
import com.team.updevic001.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(authService.verifyAndGetToken(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest) {
        AuthResponseDto response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

}
