package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;

import java.util.List;

public interface StudentService {

    void enrollInCourse(String courseId);

    void unenrollUserFromCourse(String courseId);

    ResponseCourseShortInfoDto getStudentCourse(String courseId);

    List<ResponseCourseShortInfoDto> getStudentCourses();

    List<ResponseCourseLessonDto> getStudentLessons();

    void deleteStudentCourse(String courseId);

}