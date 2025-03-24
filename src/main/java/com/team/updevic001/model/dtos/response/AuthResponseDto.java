package com.team.updevic001.model.dtos.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthResponseDto {
    String accessToken;
    String refreshToken;
}
