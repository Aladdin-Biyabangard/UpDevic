package com.team.updevic001.model.dtos.response.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCategoryDto {

    private List<ResponseCourseShortInfoDto> courses;

    private Integer courseCount;


}
