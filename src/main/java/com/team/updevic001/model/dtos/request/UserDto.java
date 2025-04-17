package com.team.updevic001.model.dtos.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Size(max = 17)
    private String firstName;
    @Size(max = 17)
    private String lastName;
    private String password;
    private String email;
}