package com.team.updevic001.model.dtos.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentDto extends UserDto {

        private UserDto userDto;

        private String studentNumber;

    }
}