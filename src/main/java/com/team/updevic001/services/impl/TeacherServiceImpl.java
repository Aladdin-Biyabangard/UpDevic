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


//    @Override
//    public List<ResponseLessonDto> getTeacherLessons(String teacherId) {
//        log.info("Getting teacher lessons. Teacher ID: {}", teacherId);
//
//        Teacher teacher = findTeacherById(teacherId);
//        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
//        List<ResponseLessonDto> lessons =
//                teacherCourses.stream()
//                        .flatMap(teacherCourse -> teacherCourse.getCourse().getLessons().stream())
//                        .map(lessonMapper::toDto).toList();
//
//        log.info("Retrieved {} lessons for teacher ID: {}", lessons.size(), teacherId);
//        return lessons;
//    }

//    @Override
//    public void deleteTeacherLesson(String teacherId, String lessonId) {
//        log.info("Deleting teacher lesson. Teacher ID: {}, Lesson ID: {}", teacherId, lessonId);
//
//        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
//        Lesson lesson = lessonRepository.findById(lessonId)
//                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found!"));
//
//        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
//
//        Optional<TeacherCourse> teacherCourse = teacherCourses.stream()
//                .filter(tc -> tc.getCourse().getLessons().contains(lesson)) // Bu dərs kursda olmalıdır
//                .findFirst();
//
//        if (teacherCourse.isPresent()) {
//            teacherCourse.get().getCourse().getLessons().remove(lesson); // Dərsi kursdan silirik
//            courseRepository.save(teacherCourse.get().getCourse()); // Kursu yeniləyirik
//            lessonRepository.delete(lesson);
//            log.info("Teacher lesson deleted successfully. Lesson ID: {}", lessonId);
//        } else {
//            throw new IllegalArgumentException("No such lesson found for this teacher.");
//        }
//    }

//
//    @Override
//    public void deleteTeacherCourses(String teacherId) {
//        log.info("Deleting all teacher courses. Teacher ID: {}", teacherId);
//        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
//        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
//        teacherCourseRepository.deleteAll(teacherCourses);
//
//        log.info("All teacher courses deleted successfully. Teacher ID: {}", teacherId);
//    }

//    @Override
//    public void deleteTeacherLessons(String teacherId) {
//        log.info("Deleting all teacher lessons. Teacher ID: {}", teacherId);
//        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
//        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
//        List<Lesson> list = teacherCourses.stream()
//                .flatMap(teacherCourse -> teacherCourse.getCourse().getLessons().stream()).toList();
//        lessonRepository.deleteAll(list);
//
//        log.info("All teacher lessons deleted successfully. Teacher ID: {}", teacherId);
//    }

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