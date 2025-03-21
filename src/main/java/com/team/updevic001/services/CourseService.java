package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;

import java.util.List;

public interface CourseService {

    ResponseCourseDto getCourse(String courseId);

    List<ResponseCourseDto> getCourses();

    List<ResponseCourseLessonDto> getCourseLessons(String courseId);

    List<ResponseCommentDto> getCourseComment(String courseId);

    void deleteCourseComment(String courseId, String commentId);


}
