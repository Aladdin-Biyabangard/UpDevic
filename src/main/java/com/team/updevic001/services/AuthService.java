package com.team.updevic001.services;

import com.team.updevic001.model.dtos.request.RecoveryPassword;
import com.team.updevic001.model.dtos.request.security.AuthRequestDto;
import com.team.updevic001.model.dtos.request.security.OtpRequest;
import com.team.updevic001.model.dtos.request.security.RegisterRequest;
import com.team.updevic001.model.dtos.response.AuthResponseDto;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
    void register(RegisterRequest request);

    AuthResponseDto login(AuthRequestDto authRequestDto);

    AuthResponseDto verifyAndGetToken(OtpRequest request);

    void requestPasswordReset(String email);

    void resetPassword(String token, RecoveryPassword recoveryPassword);
}