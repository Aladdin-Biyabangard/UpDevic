package com.team.updevic001.services.interfaces;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.CourseCategoryType;

import java.util.List;

public interface CourseService {

    ResponseCourseDto createCourse(CourseDto courseDto);

    ResponseCourseDto addTeacherToCourse(String courseId, String userId);

    ResponseCourseDto updateCourse(String courseId, CourseDto courseDto);

    ResponseCourseLessonDto getCourse(String courseId);

    List<ResponseCourseDto> getCourses();

    List<ResponseCategoryDto> getCategories();

    List<ResponseCourseShortInfoDto> findCoursesByCategory(CourseCategoryType categoryType);

    List<ResponseCourseShortInfoDto> searchCourse(String keyword);

    void deleteCourse(String courseId);


}
