package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;

import java.util.List;

public interface TeacherService {


    ResponseCourseShortInfoDto getTeacherCourse(String courseId);

    List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses();

    ResponseLessonDto getTeacherLesson(String lessonId);

    List<ResponseLessonDto> getTeacherLessonsByCourse(String courseId);

    List<ResponseLessonDto> getTeacherLessons();


    Teacher findTeacherByUserId(String userId);


    void deleteTeacherLesson(String userId, String lessonId);

    void deleteTeacherCourses(String userId);

    void deleteTeacherLessons(String userId);

    void deleteTeacher(String userId);

    void deleteAllTeachers();

    public Teacher getAuthenticatedTeacher();

}
