package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;

import java.util.List;

public interface StudentService {

    void enrollInCourse(String courseId, String userId);

    void unenrollUserFromCourse(String userId, String courseId);

    List<ResponseCourseShortInfoDto> getStudentCourse(String userId);

    List<ResponseCourseLessonDto> getStudentLesson(String userId);

    void deleteStudentCourse(String userId, String courseId);


    void deleteStudentCourseComment(String userId, String courseId, String commentId);

    void deleteStudentLessonComment(String userId, String lessonId, String commentId);


}