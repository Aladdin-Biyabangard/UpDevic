package com.team.updevic001.model.dtos.request;

import com.team.updevic001.model.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto extends UserDto {


    private Specialty specialty;

    private Integer experienceYears;

    private String socialLink;


}
