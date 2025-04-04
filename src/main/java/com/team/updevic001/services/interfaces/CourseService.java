package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.TeacherCourse;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.CourseCategoryType;

import java.util.List;

public interface CourseService {

    ResponseCourseDto createCourse(CourseDto courseDto);

    ResponseTeacherWithCourses addTeacherToCourse(String userId, String courseId);

    List<ResponseCourseDto> searchCourse(String keyword);

    void updateTeacherCourseInfo(String courseId, CourseDto courseDto);

    ResponseCourseLessonDto getCourse(String courseId);

    List<ResponseCourseDto> getCourses();

    List<ResponseCategoryDto> getCategory(CourseCategoryType categoryType);

    TeacherCourse findTeacherCourse(Course course, Teacher teacher);

    List<Course> findCourseBy(String keyword);

    Course findCourseById(String courseId);

    void deleteCourse(String courseId);

}
