package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.configuration.mappers.LessonMapper;
import com.team.updevic001.configuration.mappers.TeacherMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.TeacherService;
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

    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final TeacherMapper teacherMapper;
    private final UserRoleRepository userRoleRepository;
    private final CourseMapper courseMapper;
    private final AuthHelper authHelper;

//    @Override
//    public ResponseCourseDto createTeacherCourse(String teacherId, CourseDto courseDto) {
//        log.info("Creating a new teacher course. Teacher ID: {}, Course Title: {}", teacherId, courseDto.getTitle());
//
//        Teacher teacher = findTeacherById(teacherId);
//
//        UserRole userRole = userRoleRepository.findByName(Role.HEAD_TEACHER).orElseGet(() -> {
//            UserRole role = UserRole.builder()
//                    .name(Role.HEAD_TEACHER)
//                    .build();
//            return userRoleRepository.save(role);
//
//        });
//        teacher.getUser().getRoles().add(userRole);
//        teacherRepository.save(teacher);
////        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.FALSE);
//        Course course = modelMapper.map(courseDto, Course.class);
//        course.setCategory(CourseCategory.builder()
//                .category(courseDto.getCourseCategoryType())
//                .build());
//        courseRepository.save(course);
//        log.info("Course saved successfully. Course ID: {}", course.getUuid());
//
//        TeacherCourse teacherCourse = new TeacherCourse();
//        teacherCourse.setCourse(course);
//        teacherCourse.setTeacher(teacher);
//        teacherCourseRepository.save(teacherCourse);
//
//        log.info("TeacherCourse relationship saved successfully. Teacher ID: {}, Course ID: {}", teacherId, course.getUuid());
//        return courseMapper.courseDto(course);
//    }


    public ResponseTeacherWithCourses addTeacherToCourse(String teacherId, String courseId) {
        Teacher teacher = findTeacherById(teacherId);
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Not course found!"));

        Optional<TeacherCourse> teacherCourse = teacherCourseRepository.findByCourseAndTeacher(course, teacher);

        if (teacherCourse.isPresent()) {
            throw new IllegalArgumentException("This teacher" + teacherId + " already exists in this course." + courseId);
        } else {
            TeacherCourse newTeacherCourse = new TeacherCourse();
            newTeacherCourse.setCourse(course);
            newTeacherCourse.setTeacher(teacher);
            teacherCourseRepository.save(newTeacherCourse);
        }
        return teacherMapper.toDto(teacher);
    }

    @Override
    public ResponseLessonDto assignLessonToCourse(String teacherId, String courseId, LessonDto lessonDto) {
        log.info("Assigning lesson to course. Teacher ID: {}, Course ID: {}", teacherId, courseId);
        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.FALSE);

        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);

        Course course = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

        findTeacherCourse(course, teacher);

        lesson.setCourse(course);
        course.getLessons().add(lesson);
        lessonRepository.save(lesson);

        log.info("Lesson assigned successfully. Lesson ID: {}", lesson.getUuid());
        return modelMapper.map(lesson, ResponseLessonDto.class);
    }

//    @Override
//    public void updateTeacherCourseInfo(String courseId, CourseDto courseDto) {
//        log.info("Updating teacher course info. Teacher ID: {}, Course ID: {}", teacherId, courseId);
//        User authenticatedUser = authHelper.getAuthenticatedUser();
//        Teacher teacher = authenticatedUser.getTeacher();
//
////        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.FALSE);
//        Course findCourse = courseRepository
//                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));
//
//        TeacherCourse teacherCourse = findTeacherCourse(findCourse, teacher);
//
//        modelMapper.map(courseDto, findCourse);
//
//        courseRepository.save(findCourse);
//        teacherCourse.setCourse(findCourse);
//        teacherCourseRepository.save(teacherCourse);
//
//        log.info("Teacher course updated successfully. Course ID: {}", findCourse.getUuid());
//    }
//
//    @Override
//    public void updateTeacherLessonInfo(String teacherId, String lessonId, LessonDto updatedLessonDto) {
//        log.info("Updating teacher lesson info. Teacher ID: {}, Lesson ID: {}", teacherId, lessonId);
//
//        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.FALSE);
//        Lesson lesson = findLessonById(lessonId);
//
//        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
//
//        Optional<TeacherCourse> teacherCourse = teacherCourses.stream().filter(tc -> tc.getCourse().getLessons().contains(lesson)).findFirst();
//
//        if (teacherCourse.isPresent()) {
//            modelMapper.map(updatedLessonDto, lesson);
//            lessonRepository.save(lesson);
//        } else {
//            throw new IllegalArgumentException("No such lesson found for this teacher.");
//        }
//    }

    @Override
    public ResponseCourseShortInfoDto getTeacherCourse(String teacherId, String courseId) {
        log.info("Getting teacher course. Teacher ID: {}, Course ID: {}", teacherId, courseId);

        Teacher teacher = findTeacherById(teacherId);
        Course findCourse = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));
        findTeacherCourse(findCourse, teacher);

        log.info("Teacher course retrieved successfully. Course ID: {}", findCourse.getUuid());
        return modelMapper.map(findCourse, ResponseCourseShortInfoDto.class);
    }

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
    public ResponseLessonDto getTeacherLesson(String teacherId, String lessonId) {
        log.info("Getting teacher lesson. Teacher ID: {}, Lesson ID: {}", teacherId, lessonId);

        Teacher teacher = findTeacherById(teacherId);

        Lesson findLesson = lessonRepository
                .findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found!"));

        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        Optional<TeacherCourse> teacherCourse = teacherCourses.stream()
                .filter(tc -> tc.getCourse().getLessons().contains(findLesson))
                .findFirst();

        if (teacherCourse.isPresent()) {
            log.info("Teacher lesson retrieved successfully. Lesson ID: {}", findLesson.getUuid());
            return modelMapper.map(findLesson, ResponseLessonDto.class);
        } else {
            throw new IllegalArgumentException("No such lesson found for this teacher.");
        }
    }


    @Override
    public List<ResponseLessonDto> getTeacherLessonsByCourse(String teacherId, String courseId) {
        log.info("Getting teacher lessons by course. Teacher ID: {}, Course ID: {}", teacherId, courseId);

        Teacher teacher = findTeacherById(teacherId);
        Course findCourse = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

        findTeacherCourse(findCourse, teacher);
        List<Lesson> lessons = findCourse.getLessons();

        log.info("Retrieved {} lessons for teacher ID: {}, Course ID: {}", lessons.size(), teacherId, courseId);
        return lessons.isEmpty() ? List.of() : lessonMapper.toDto(lessons);
    }

    @Override
    public List<ResponseLessonDto> getTeacherLessons(String teacherId) {
        log.info("Getting teacher lessons. Teacher ID: {}", teacherId);

        Teacher teacher = findTeacherById(teacherId);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
        List<ResponseLessonDto> lessons =
                teacherCourses.stream()
                        .flatMap(teacherCourse -> teacherCourse.getCourse().getLessons().stream())
                        .map(lessonMapper::toDto).toList();

        log.info("Retrieved {} lessons for teacher ID: {}", lessons.size(), teacherId);
        return lessons;
    }

//    @Override
//    public void deleteTeacherCourse(String teacherId, String courseId) {
//        log.info("Deleting teacher course. Teacher ID: {}, Course ID: {}", teacherId, courseId);
//
//        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
//        Course course = findCourseById(courseId);
//        TeacherCourse teacherCourse = findTeacherCourse(course, teacher);
//        teacherCourseRepository.delete(teacherCourse);
//        courseRepository.delete(course);
//
//        log.info("Teacher course deleted successfully. Teacher ID: {}, Course ID: {}", teacherId, courseId);
//    }

    @Override
    public void deleteTeacherLesson(String teacherId, String lessonId) {
        log.info("Deleting teacher lesson. Teacher ID: {}, Lesson ID: {}", teacherId, lessonId);

        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found!"));

        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        Optional<TeacherCourse> teacherCourse = teacherCourses.stream()
                .filter(tc -> tc.getCourse().getLessons().contains(lesson)) // Bu dərs kursda olmalıdır
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
    public void deleteTeacherCourses(String teacherId) {
        log.info("Deleting all teacher courses. Teacher ID: {}", teacherId);
        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
        teacherCourseRepository.deleteAll(teacherCourses);

        log.info("All teacher courses deleted successfully. Teacher ID: {}", teacherId);
    }

    @Override
    public void deleteTeacherLessons(String teacherId) {
        log.info("Deleting all teacher lessons. Teacher ID: {}", teacherId);
        Teacher teacher = validateTeacherAndAccess(teacherId, Boolean.TRUE);
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);
        List<Lesson> list = teacherCourses.stream()
                .flatMap(teacherCourse -> teacherCourse.getCourse().getLessons().stream()).toList();
        lessonRepository.deleteAll(list);

        log.info("All teacher lessons deleted successfully. Teacher ID: {}", teacherId);
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

    private Course findCourseById(String courseId) {
        log.info("Finding course by ID: {}", courseId);
        return courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));
    }

    public Lesson findLessonById(String lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found these Id"));
    }

    private TeacherCourse findTeacherCourse(Course course, Teacher teacher) {
        log.info("Finding teacher-course relationship. Teacher ID: {}, Course ID: {}", teacher.getUuid(), course.getUuid());
        return teacherCourseRepository.findByCourseAndTeacher(course, teacher)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher with ID: " + teacher.getUuid() + " is not assigned to course with ID: " + course.getUuid()));
    }
}