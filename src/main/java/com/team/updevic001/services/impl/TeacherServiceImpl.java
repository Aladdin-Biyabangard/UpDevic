package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.configuration.mappers.LessonMapper;
import com.team.updevic001.configuration.mappers.TeacherMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.interfaces.TeacherService;
import com.team.updevic001.services.interfaces.VideoService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final CourseServiceImpl courseServiceImpl;
    private final LessonServiceImpl lessonServiceImpl;
    private final AuthHelper authHelper;
    @Value("${video.directory}")
    private String VIDEO_DIRECTORY;

    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final TeacherMapper teacherMapper;
    private final CourseCategoryRepository courseCategoryRepository;
    private final UserRoleRepository userRoleRepository;
    private final CourseMapper courseMapper;
    private final VideoService videoServiceImpl;

    @Override
    public ResponseCourseDto createCourse(String userId, CourseDto courseDto) {
        log.info("Creating a new teacher course. Teacher ID: {}, Course Title: {}", userId, courseDto.getTitle());

        Teacher teacher = findTeacherByUserId(userId);
        UserRole userRole = userRoleRepository.findByName(Role.HEAD_TEACHER).orElseGet(() -> {
            UserRole role = UserRole.builder().name(Role.HEAD_TEACHER).build();
            return userRoleRepository.save(role);

        });
        teacher.getUser().getRoles().add(userRole);
        teacherRepository.save(teacher);
        Course course = modelMapper.map(courseDto, Course.class);
        CourseCategory category = CourseCategory.builder().category(courseDto.getCourseCategoryType()).build();
        courseCategoryRepository.save(category);

        course.setStatus(Status.CREATED);
        course.setCategory(category);
        courseRepository.save(course);
        log.info("Course saved successfully. Course ID: {}", course.getId());

        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setCourse(course);
        teacherCourse.setTeacher(teacher);
        teacherCourseRepository.save(teacherCourse);

        log.info("TeacherCourse relationship saved successfully. Teacher ID: {}, Course ID: {}", teacher.getId(), course.getId());
        return courseMapper.courseDto(course);
    }


    public ResponseTeacherWithCourses addTeacherToCourse(String userId, String courseId) {
        Teacher teacher = findTeacherByUserId(userId);

        Course course = courseServiceImpl.findCourseById(courseId);

        Optional<TeacherCourse> teacherCourse = teacherCourseRepository.findByCourseAndTeacher(course, teacher);

        if (teacherCourse.isPresent()) {
            throw new IllegalArgumentException("This teacher" + teacher.getId() + " already exists in this course." + courseId);
        } else {
            TeacherCourse newTeacherCourse = new TeacherCourse();
            newTeacherCourse.setCourse(course);
            newTeacherCourse.setTeacher(teacher);
            teacherCourseRepository.save(newTeacherCourse);
        }
        return teacherMapper.toDto(teacher);
    }


    @Override
    public ResponseLessonShortInfoDto assignLessonToCourse(String courseId, LessonDto lessonDto, MultipartFile file) throws Exception {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Assigning lesson to course. Teacher ID: {}, Course ID: {}", authenticatedUser.getId(), courseId);
        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());

        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);

        Course course = courseServiceImpl.findCourseById(courseId);

        findTeacherCourse(course, teacher);


        if (file != null && !file.isEmpty()) {
            String uploadedFileName = videoServiceImpl.uploadVideo(file);
            lesson.setVideoUrl(VIDEO_DIRECTORY + uploadedFileName);
        }

        lesson.setCourse(course);
        course.getLessons().add(lesson);
        lessonRepository.save(lesson);

        log.info("Lesson assigned successfully. Lesson ID: {}", lesson.getId());
        return modelMapper.map(lesson, ResponseLessonShortInfoDto.class);
    }

    @Override
    public void updateTeacherCourseInfo(String courseId, CourseDto courseDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Updating teacher course info. Teacher ID: {}, Course ID: {}", authenticatedUser.getId(), courseId);
        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());

        Course course = courseServiceImpl.findCourseById(courseId);
        TeacherCourse teacherCourse = findTeacherCourse(course, teacher);

        modelMapper.map(courseDto, course);

        courseRepository.save(course);
        teacherCourse.setCourse(course);
        teacherCourseRepository.save(teacherCourse);
        log.info("Teacher course updated successfully. Course ID: {}", course.getId());
    }

    @Override
    public void updateTeacherLessonInfo(String lessonId, LessonDto updatedLessonDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Updating teacher lesson info. Teacher ID: {}, Lesson ID: {}", authenticatedUser.getId(), lessonId);

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());

        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);

        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        Optional<TeacherCourse> teacherCourse = teacherCourses.stream().filter(tc -> tc.getCourse().getLessons().contains(lesson)).findFirst();

        if (teacherCourse.isPresent()) {
            modelMapper.map(updatedLessonDto, lesson);
            lessonRepository.save(lesson);
        } else {
            throw new IllegalArgumentException("No such lesson found for this teacher.");
        }
    }

    @Override
    public ResponseCourseShortInfoDto getTeacherCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting teacher course. Teacher ID: {}, Course ID: {}", authenticatedUser.getId(), courseId);

        Teacher teacher = findTeacherByUserId(authenticatedUser.getId());
        Course course = courseServiceImpl.findCourseById(courseId);
        findTeacherCourse(course, teacher);

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

        findTeacherCourse(findCourse, teacher);
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
    public void deleteTeacherCourse(String userId, String courseId) {
        log.info("Deleting teacher course. Teacher ID: {}, Course ID: {}", userId, courseId);

        Teacher teacher = findTeacherByUserId(userId);
        Course course = courseServiceImpl.findCourseById(courseId);
        TeacherCourse teacherCourse = findTeacherCourse(course, teacher);
        teacherCourseRepository.delete(teacherCourse);
        courseRepository.delete(course);

        log.info("Teacher course deleted successfully. Teacher ID: {}, Course ID: {}", teacher.getId(), courseId);
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
        teacherRepository.deleteById(userId);
    }

    @Override
    public void deleteAllTeachers() {
        teacherRepository.deleteAll();
        teacherRepository.resetAutoIncrement();
    }

    @Override
    public TeacherCourse findTeacherCourse(Course course, Teacher teacher) {
        log.info("Finding teacher-course relationship. Teacher ID: {}, Course ID: {}", teacher.getId(), course.getId());
        return teacherCourseRepository.findByCourseAndTeacher(course, teacher).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID: " + teacher.getId() + " is not assigned to course with ID: " + course.getId()));
    }

    @Override
    public Teacher findTeacherByUserId(String userId) {
        log.info("Finding teacher by ID: {}", userId);
        return teacherRepository.findTeacherByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("NOT_TEACHER_FOUND"));

    }
}