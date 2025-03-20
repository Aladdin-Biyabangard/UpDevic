package com.team.updevic001.model.dtos.response.course;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.model.enums.CourseCategory;

import java.util.List;

public class ResponseCategoryDto {

    private CourseCategory category;

    @JsonManagedReference
    private List<Course> courses;

    private Integer courseCount;
}
