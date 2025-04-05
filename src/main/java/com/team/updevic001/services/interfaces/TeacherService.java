package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;

import java.util.List;

public interface TeacherService {

    List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses(String teacherId);

//    Teacher findTeacherByUserId(String userId);

    Teacher getAuthenticatedTeacher();

    void deleteTeacher(String teacherId);

    void deleteAllTeachers();

}
