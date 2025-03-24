package com.team.updevic001.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCheckDto {

    private String firstName;
    private String lastName;
    private int experienceYear;
    private String education;
    private String workplace;
    private String socialLink;
    private String email;
}
