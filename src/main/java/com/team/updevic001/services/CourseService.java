package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.enums.CourseCategoryType;

import java.util.List;

public interface CourseService {

    List<ResponseCourseDto> getCourse(String courseId);

    List<ResponseCourseDto> getCourses();

    List<ResponseCategoryDto> getCategory(CourseCategoryType categoryType);

    List<ResponseLessonDto> getCourseLessons(String courseId);

    List<ResponseCommentDto> getCourseComment(String courseId);

    void deleteCourseComment(String courseId, String commentId);


}
