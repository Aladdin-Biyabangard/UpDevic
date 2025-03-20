package com.team.updevic001.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto extends UserDto {

    private String profilePicture;

    private String bio;

    private List<String> socialLinks;

    private List<String> skills;
}
