package com.team.updevic001.services;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;

import java.util.List;

public interface TeacherService {

//    ResponseCourseDto createTeacherCourse(String teacherId, CourseDto courseDto);

    ResponseTeacherWithCourses addTeacherToCourse(String teacherId, String courseId);

    ResponseLessonDto assignLessonToCourse(String teacherId, String courseId, LessonDto lessonDto);

//    void updateTeacherCourseInfo(String courseId, CourseDto courseDto);
//
//    void updateTeacherLessonInfo(String teacherId, String lessonId, LessonDto lessonDto);

    ResponseCourseShortInfoDto getTeacherCourse(String teacherId, String courseId);

    List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses(String teacherId);

    ResponseLessonDto getTeacherLesson(String teacherId, String lessonId);

    List<ResponseLessonDto> getTeacherLessonsByCourse(String teacherId, String courseId);

    List<ResponseLessonDto> getTeacherLessons(String teacherId);

//    void deleteTeacherCourse(String teacherId, String courseId);

    void deleteTeacherLesson(String teacherId, String lessonId);

    void deleteTeacherCourses(String teacherId);

    void deleteTeacherLessons(String teacherId);

    void deleteTeacher(String teacherId);

    void deleteAllTeachers();

}
