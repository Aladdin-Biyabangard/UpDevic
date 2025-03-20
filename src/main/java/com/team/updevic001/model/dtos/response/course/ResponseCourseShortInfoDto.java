package com.team.updevic001.model.dtos.response.course;

import com.team.updevic001.model.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCourseShortInfoDto {

    private String title;

    private String description;

    private CourseLevel level;
}
