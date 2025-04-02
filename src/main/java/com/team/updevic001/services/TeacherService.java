package com.team.updevic001.services;

import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;

import java.util.List;

public interface TeacherService {

    List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses(String teacherId);

    void deleteTeacher(String teacherId);

    void deleteAllTeachers();

    Teacher getAuthenticatedTeacher();

}
