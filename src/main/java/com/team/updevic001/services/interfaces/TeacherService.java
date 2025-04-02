package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.TeacherCourse;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {

    ResponseCourseDto createCourse(String userId, CourseDto courseDto);

    ResponseTeacherWithCourses addTeacherToCourse(String userId, String courseId);

    ResponseLessonShortInfoDto assignLessonToCourse(String courseId, LessonDto lessonDto, MultipartFile file) throws Exception;

    void updateTeacherCourseInfo(String courseId, CourseDto courseDto);

    void updateTeacherLessonInfo(String lessonId, LessonDto lessonDto);

    ResponseCourseShortInfoDto getTeacherCourse(String courseId);

    List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses();

    ResponseLessonDto getTeacherLesson(String lessonId);

    List<ResponseLessonDto> getTeacherLessonsByCourse(String courseId);

    List<ResponseLessonDto> getTeacherLessons();

    TeacherCourse findTeacherCourse(Course course, Teacher teacher);

    Teacher findTeacherByUserId(String userId);


    void deleteTeacherCourse(String userId, String courseId);

    void deleteTeacherLesson(String userId, String lessonId);

    void deleteTeacherCourses(String userId);

    void deleteTeacherLessons(String userId);

    void deleteTeacher(String userId);

    void deleteAllTeachers();

}
