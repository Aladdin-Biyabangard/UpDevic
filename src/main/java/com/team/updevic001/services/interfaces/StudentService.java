package com.team.updevic001.services.interfaces;

import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;

import java.util.List;

public interface StudentService {

    void enrollInCourse(String courseId, String userId);

    void unenrollUserFromCourse(String userId, String courseId);

    ResponseCourseShortInfoDto getStudentCourse(String userId, String courseId);

    List<ResponseCourseShortInfoDto> getStudentCourses(String userId);

    List<ResponseCourseLessonDto> getStudentLessons(String userId);

    void deleteStudentCourseComment(String userId, String courseId, String commentId);

    void deleteStudentLessonComment(String userId, String lessonId, String commentId);


}