package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.enums.CourseCategoryType;

import java.util.List;

public interface CourseService {

    List<ResponseCourseDto> searchCourse(String keyword);

    ResponseCourseLessonDto getCourse(String courseId);

    List<ResponseCourseDto> getCourses();

    List<ResponseCategoryDto> getCategory(CourseCategoryType categoryType);

}
