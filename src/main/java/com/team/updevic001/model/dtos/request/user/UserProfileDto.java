package com.team.updevic001.model.dtos.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private String firstName;
    private String lastName;
    private String password;

    private String profilePicture;

    private String bio;

    private List<String> socialLinks;

    private List<String> skills;
}
