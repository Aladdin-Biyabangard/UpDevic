package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.TeacherCourse;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.repositories.TeacherCourseRepository;
import com.team.updevic001.dao.repositories.TeacherRepository;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.interfaces.TeacherService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final AuthHelper authHelper;


    private final TeacherRepository teacherRepository;
    private final TeacherCourseRepository teacherCourseRepository;

    @Override
    public List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses() {
        Teacher authenticatedTeacher = getAuthenticatedTeacher();
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(authenticatedTeacher);

        List<ResponseCourseShortInfoDto> courses = teacherCourses.stream()
                .map(teacherCourse -> {
                    Course course = teacherCourse.getCourse();
                    return new ResponseCourseShortInfoDto(course.getId(), course.getTitle(), course.getLevel());
                })
                .toList();

        log.info("Retrieved {} courses for teacher ID: {}", courses.size(), authenticatedTeacher);
        return courses;
    }


    @Override
    public void deleteTeacher(String teacherId) {
        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
        teacherRepository.delete(teacher);
    }

    @Override
    public void deleteAllTeachers() {
        teacherRepository.deleteAll();
        teacherRepository.resetAutoIncrement();
    }

    private Teacher validateTeacherAndAccess(String teacherId, boolean isAllowedToAdmin) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Teacher teacher = findTeacherById(teacherId);

        boolean isOwner = teacher.getUser().getId().equals(authenticatedUser.getId());
        boolean isAdmin = isAllowedToAdmin && authenticatedUser.getRoles().stream()
                .anyMatch(userRole -> userRole.getName().equals(Role.ADMIN));

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("NOT_ALLOWED");
        }

        return teacher;
    }

    private Teacher findTeacherById(String teacherID) {
        log.info("Finding teacher by ID: {}", teacherID);
        return teacherRepository.findById(teacherID)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found this id: " + teacherID));
    }

    @Override
    public Teacher findTeacherByUserId(String userId) {
        log.info("Finding teacher by ID: {}", userId);
        return teacherRepository.findTeacherByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("NOT_TEACHER_FOUND"));

    }

    public Teacher getAuthenticatedTeacher() {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());
        if (teacher == null) {
            log.info("User is not teacher");
            throw new ForbiddenException("NOT_ALLOWED");
        }
        return teacher;
    }


}