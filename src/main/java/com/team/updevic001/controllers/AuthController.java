package com.team.updevic001.controllers;

import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.dao.repositories.TeacherCourseRepository;
import com.team.updevic001.model.dtos.request.security.*;
import com.team.updevic001.model.dtos.response.AuthResponseDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
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


    AuthService authService;
    CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;

    @PutMapping(path = "create-admin")
    AuthResponseDto createUserWithAdminRole(AuthRequestDto authRequest) {
        return authService.createUserWithAdminRole(authRequest);
    }

    @GetMapping(path = "")
    public ResponseUserDto getLoggedInUser() {
        return authService.getLoggedInUser();
    }

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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestPasswordReset(@RequestParam @NotBlank String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset token has been sent to user's email");
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam @NotBlank String token, @Valid @RequestBody RecoveryPassword recoveryPassword) {
        authService.resetPassword(token, recoveryPassword);
        return ResponseEntity.ok("Password has been successfully reset.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequest request) {
        return new ResponseEntity<>(authService.refreshAccessToken(request), HttpStatus.OK);
    }

    @DeleteMapping(path = "all-course-delete")
    public ResponseEntity<String> deleteAll() {
        teacherCourseRepository.deleteAll();
        courseRepository.deleteAll();
        return ResponseEntity.ok("Delete all");
    }

}
