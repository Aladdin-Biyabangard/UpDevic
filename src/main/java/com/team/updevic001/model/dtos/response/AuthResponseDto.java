package com.team.updevic001.model.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    String accessToken;
    String refreshToken;
}
