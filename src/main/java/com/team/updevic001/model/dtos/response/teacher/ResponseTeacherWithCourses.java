package com.team.updevic001.model.dtos.response.teacher;

import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTeacherWithCourses extends ResponseUserDto {

    private Specialty speciality;
    private Integer experienceYears;
    private List<ResponseCourseDto> courseDtoS;
}
