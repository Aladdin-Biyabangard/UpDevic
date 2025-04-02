package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.TeacherService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final AuthHelper authHelper;


    @Override
    public List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses(String teacherId) {
        log.info("Getting teacher and related courses. Teacher ID: {}", teacherId);

        Teacher teacher = findTeacherById(teacherId);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        List<ResponseCourseShortInfoDto> courses = teacherCourses.stream()
                .map(teacherCourse -> {
                    Course course = teacherCourse.getCourse();
                    return new ResponseCourseShortInfoDto(course.getUuid(), course.getTitle(), course.getLevel());
                })
                .toList();

        log.info("Retrieved {} courses for teacher ID: {}", courses.size(), teacherId);
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

        boolean isOwner = teacher.getUser().getUuid().equals(authenticatedUser.getUuid());
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

    public Teacher getAuthenticatedTeacher() {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Teacher teacher = authenticatedUser.getTeacher();
        if (teacher == null) {
            log.info("User is not teacher");
            throw new ForbiddenException("NOT_ALLOWED");
        }
        return teacher;
    }
}