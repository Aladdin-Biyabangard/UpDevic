package com.team.updevic001.services;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.CourseCategoryType;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {

    List<ResponseCourseDto> searchCourse(String keyword);

    ResponseCourseLessonDto getCourse(String courseId);

    List<ResponseCourseDto> getCourses();

    List<ResponseCategoryDto> getCategory(CourseCategoryType categoryType);

    ResponseCourseDto createCourse(CourseDto courseDto);

    ResponseTeacherWithCourses addTeacherToCourse(String courseId, String userId);

    void deleteCourse(String courseId);

    ResponseCourseDto updateCourse(String courseId, CourseDto courseDto);

    List<ResponseLessonDto> getLessonsByCourse(String courseId);

    ResponseLessonDto assignLessonToCourse(String courseId, LessonDto lessonDto);

    ResponseLessonDto getLessonOfCourse(String courseId, String lessonId);

    void deleteLesson(String courseId, String lessonId);

    ResponseLessonDto updateLessonInfo(String courseId, String lessonId, LessonDto lessonDto);
}
