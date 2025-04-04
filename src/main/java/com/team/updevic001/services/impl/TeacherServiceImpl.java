package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.LessonMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.dao.repositories.LessonRepository;
import com.team.updevic001.dao.repositories.TeacherCourseRepository;
import com.team.updevic001.dao.repositories.TeacherRepository;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.services.interfaces.CourseService;
import com.team.updevic001.services.interfaces.LessonService;
import com.team.updevic001.services.interfaces.TeacherService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {


    private final CourseService courseService;
    private final LessonService lessonServiceImpl;
    private final AuthHelper authHelper;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;


    @Override
    public ResponseCourseShortInfoDto getTeacherCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting teacher course. Teacher ID: {}, Course ID: {}", authenticatedUser.getId(), courseId);

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());
        Course course = courseService.findCourseById(courseId);
        courseService.findTeacherCourse(course, teacher);

        log.info("Teacher course retrieved successfully. Course ID: {}", course.getId());
        return modelMapper.map(course, ResponseCourseShortInfoDto.class);
    }

    @Override
    public List<ResponseCourseShortInfoDto> getTeacherAndRelatedCourses() {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting teacher and related courses. Teacher ID: {}", authenticatedUser.getId());

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        List<ResponseCourseShortInfoDto> courses = teacherCourses.stream().map(teacherCourse -> {
            Course course = teacherCourse.getCourse();
            return new ResponseCourseShortInfoDto(course.getId(), course.getTitle(), course.getLevel());
        }).toList();

        log.info("Retrieved {} courses for teacher ID: {}", courses.size(), teacher.getId());
        return courses;
    }

    @Override
    public ResponseLessonDto getTeacherLesson(String lessonId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting teacher lesson. Teacher ID: {}, Lesson ID: {}", authenticatedUser.getId(), lessonId);

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());

        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        Optional<TeacherCourse> teacherCourse = teacherCourses.stream().filter(tc -> tc.getCourse().getLessons().contains(lesson)).findFirst();

        if (teacherCourse.isPresent()) {
            log.info("Teacher lesson retrieved successfully. Lesson ID: {}", lesson.getId());
            return modelMapper.map(lesson, ResponseLessonDto.class);
        } else {
            throw new IllegalArgumentException("No such lesson found for this teacher.");
        }
    }


    @Override
    public List<ResponseLessonDto> getTeacherLessonsByCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting teacher lessons by course. Teacher ID: {}, Course ID: {}", authenticatedUser.getId(), courseId);

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());
        Course findCourse = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

        courseService.findTeacherCourse(findCourse, teacher);
        List<Lesson> lessons = lessonServiceImpl.getLessonsByCourse(courseId);

        log.info("Retrieved {} lessons for teacher ID: {}, Course ID: {}", lessons.size(), teacher.getId(), courseId);
        return lessons.isEmpty() ? List.of() : lessonMapper.toDto(lessons);
    }

    @Override
    public List<ResponseLessonDto> getTeacherLessons() {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting teacher lessons. Teacher ID: {}", authenticatedUser.getId());

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
        List<ResponseLessonDto> lessons = teacherCourses.stream().flatMap(teacherCourse -> teacherCourse.getCourse().getLessons().stream()).map(lessonMapper::toDto).toList();

        log.info("Retrieved {} lessons for teacher ID: {}", lessons.size(), teacher.getId());
        return lessons;
    }


    @Override
    public void deleteTeacherLesson(String userId, String lessonId) {
        log.info("Deleting teacher lesson. Teacher ID: {}, Lesson ID: {}", userId, lessonId);

        Teacher teacher = findTeacherByUserId(userId);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found!"));

        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        Optional<TeacherCourse> teacherCourse = teacherCourses.stream().filter(tc -> tc.getCourse().getLessons().contains(lesson)) // Bu dərs kursda olmalıdır
                .findFirst();

        if (teacherCourse.isPresent()) {
            teacherCourse.get().getCourse().getLessons().remove(lesson); // Dərsi kursdan silirik
            courseRepository.save(teacherCourse.get().getCourse()); // Kursu yeniləyirik
            lessonRepository.delete(lesson);
            log.info("Teacher lesson deleted successfully. Lesson ID: {}", lessonId);
        } else {
            throw new IllegalArgumentException("No such lesson found for this teacher.");
        }
    }


    @Override
    public void deleteTeacherCourses(String userId) {
        log.info("Deleting all teacher courses. Teacher ID: {}", userId);

        Teacher teacher = findTeacherByUserId(userId);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
        teacherCourseRepository.deleteAll(teacherCourses);

        log.info("All teacher courses deleted successfully. Teacher ID: {}", teacher.getId());
    }

    @Override
    public void deleteTeacherLessons(String userId) {
        log.info("Deleting all teacher lessons. Teacher ID: {}", userId);

        Teacher teacher = findTeacherByUserId(userId);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
        List<Lesson> list = teacherCourses.stream().flatMap(teacherCourse -> teacherCourse.getCourse().getLessons().stream()).toList();
        lessonRepository.deleteAll(list);

        log.info("All teacher lessons deleted successfully. Teacher ID: {}", teacher.getId());
    }

    @Override
    public void deleteTeacher(String userId) {
        Teacher teacher = findTeacherByUserId(userId);
        //   Teacher teacher = validateTeacherAndAccess(userId, Boolean.TRUE);
        teacherRepository.delete(teacher);
    }

    @Override
    public void deleteAllTeachers() {
        teacherRepository.deleteAll();
        teacherRepository.resetAutoIncrement();
    }

    @Override
    public Teacher findTeacherByUserId(String userId) {
        log.info("Finding teacher by ID: {}", userId);
        return teacherRepository.findTeacherByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("NOT_TEACHER_FOUND"));

    }
/*
    private Teacher validateTeacherAndAccess(String teacherId, boolean isAllowedToAdmin) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Teacher teacher = findTeacherByUserId(teacherId);

        boolean isOwner = teacher.getUser().getId().equals(authenticatedUser.getId());
        boolean isAdmin = isAllowedToAdmin && authenticatedUser.getRoles().stream()
                .anyMatch(userRole -> userRole.getName().equals(Role.ADMIN));

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("NOT_ALLOWED");
        }

        return teacher;
    }
*/

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