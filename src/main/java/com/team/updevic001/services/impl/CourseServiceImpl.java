package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CategoryMapper;
import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.configuration.mappers.LessonMapper;
import com.team.updevic001.configuration.mappers.TeacherMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.AlreadyExistsException;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.model.enums.TeacherPermission;
import com.team.updevic001.model.enums.TeacherPrivileges;
import com.team.updevic001.services.CourseService;
import com.team.updevic001.services.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final ModelMapper modelMapper;
    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;

    @Override
    public List<ResponseCourseDto> searchCourse(String keyword) {
        List<Course> courses = findCourseBy(keyword);
        return !courses.isEmpty() ? courseMapper.courseDto(courses) : List.of();
    }

    @Override
    public ResponseCourseLessonDto getCourse(String courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courseMapper.toDto(course);
    }

    @Override
    public List<ResponseCourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return !courses.isEmpty() ? courseMapper.courseDto(courses) : List.of();
    }


    @Override
    public List<ResponseCategoryDto> getCategory(CourseCategoryType categoryType) {
        String category = categoryType.name();
        List<CourseCategory> courseCategories = courseCategoryRepository.searchCategoryByKeyword(category);
        return courseCategories.stream().map(categoryMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ResponseCourseDto createCourse(CourseDto courseDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of creating new course started by user with ID {}(whose teacher ID is {}", authenticatedTeacher.getUser().getUuid(), authenticatedTeacher.getUuid());
        Course course = modelMapper.map(courseDto, Course.class);
        course.setCategory(courseCategoryRepository.save(CourseCategory.builder()
                .category(courseDto.getCourseCategoryType())
                .build()));
        courseRepository.save(course);
        log.info("Course saved successfully. Course ID: {}", course.getUuid());

        TeacherCourse teacherCourse = TeacherCourse.builder()
                .teacher(authenticatedTeacher)
                .course(course)
                .teacherPrivilege(TeacherPrivileges.HEAD_TEACHER)
                .build();
        teacherCourseRepository.save(teacherCourse);
        log.info("TeacherCourse relationship saved successfully. Teacher ID: {}, Course ID: {}", authenticatedTeacher.getUuid(), course.getUuid());
        ResponseCourseDto responseCourseDto = courseMapper.courseDto(course);
        log.info("Operation of creating course ended successfully");
        return responseCourseDto;
    }


    @Override
    @Transactional
    public ResponseTeacherWithCourses addTeacherToCourse(String courseId, String userId) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of adding new teacher with user ID {} to course with ID {} started by user with ID {}(whose teacher ID is {}", userId, courseId, authenticatedTeacher.getUser().getUuid(), authenticatedTeacher.getUuid());
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(Course.class));
        Teacher newTeacher = teacherRepository.findByUserUuid(userId).orElseThrow(() -> new ResourceNotFoundException(Teacher.class));

        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);

        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.ADD_TEACHER)) {
            log.error("Teacher with ID {} doesn't have permission to add new teacher", authenticatedTeacher.getUuid());
            throw new ForbiddenException("NOT_ALLOWED");
        }

        if (course.getTeacherCourses().stream().anyMatch(teacherCourse1 -> teacherCourse1.getTeacher().getUuid().equals(newTeacher.getUuid()))) {
            log.info("Teacher with ID {} is already teacher in course with ID {}", newTeacher.getUuid(), courseId);
            throw new AlreadyExistsException("TEACHER_ALREADY_EXISTS_IN_THIS_COURSE");
        }

        TeacherCourse newTeacherCourseRelation = TeacherCourse.builder()
                .course(course)
                .teacher(newTeacher)
                .teacherPrivilege(TeacherPrivileges.ASSISTANT_TEACHER)
                .build();
        teacherCourseRepository.save(newTeacherCourseRelation);
        log.info("New teacher successfully added to course");
        return teacherMapper.toDto(newTeacher);
    }


    @Override
    @Transactional
    public ResponseCourseDto updateCourse(String courseId, CourseDto courseDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of updating course with ID {} started by user with ID {}(whose teacher ID is {}", courseId, authenticatedTeacher.getUser().getUuid(), authenticatedTeacher.getUuid());
        Course findCourse = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        modelMapper.map(courseDto, findCourse);

        courseRepository.save(findCourse);
        teacherCourse.setCourse(findCourse);
        teacherCourseRepository.save(teacherCourse);

        log.info("Teacher course updated successfully. Course ID: {}", findCourse.getUuid());
        return courseMapper.courseDto(findCourse);
    }

    @Override
    public List<ResponseLessonDto> getLessonsByCourse(String courseId) {
        log.info("Getting lessons of course.Course ID: {}", courseId);

        Course findCourse = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

        List<Lesson> lessons = findCourse.getLessons();

        log.info("Retrieved {} lessons of course ID: {}", lessons.size(), courseId);
        return lessons.isEmpty() ? List.of() : lessonMapper.toDto(lessons);
    }


    @Override
    @Transactional
    public ResponseLessonDto assignLessonToCourse(String courseId, LessonDto lessonDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Assigning lesson to course. Teacher ID: {},User ID {}, Course ID: {}", authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid(), courseId);

        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(Course.class));

        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);

        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.ADD_LESSON)) {
            log.error("Failed to add new lesson to course with ID {}: Teacher with ID {} doesn't have permission to add lesson", courseId, authenticatedTeacher.getUuid());
            throw new ForbiddenException("NOT_ALLOWED");
        }

        lesson.setCourse(course);
        course.getLessons().add(lesson);
        lessonRepository.save(lesson);

        log.info("Lesson assigned successfully. Lesson ID: {}", lesson.getUuid());
        return modelMapper.map(lesson, ResponseLessonDto.class);
    }

    @Override
    public ResponseLessonDto getLessonOfCourse(String courseId, String lessonId) {
        log.info("Getting lesson of course. Course ID: {}, Lesson ID: {}", courseId, lessonId);

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(Course.class));

        Lesson lesson = lessonRepository.findByUuidAndCourse(lessonId, course).orElseThrow(() -> new ResourceNotFoundException(Lesson.class));

        log.info("Lesson with ID {} of course with ID {} returned successfully", lessonId, courseId);

        return modelMapper.map(lesson, ResponseLessonDto.class);

    }

    @Override
    @Transactional
    public void deleteLesson(String courseId, String lessonId) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of deleting lesson with ID {} of course with ID {} started by teacher with ID {}(whose user ID is {})", lessonId, courseId, authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid());
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(Course.class));
        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        Lesson lesson = lessonRepository.findByUuidAndCourse(lessonId, course).orElseThrow(() -> new ResourceNotFoundException(Lesson.class));
        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.DELETE_COURSE) || !lesson.getTeacher().getUuid().equals(authenticatedTeacher.getUuid())) {
            log.error("Teacher with ID {}(whose user ID is {}) is not allowed to delete the lesson with ID {}", authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid(), lessonId);
            throw new ForbiddenException("NOT_ALLOWED_DELETE_LESSON");
        }
        lessonRepository.delete(lesson);
        log.info("Lesson successfully deleted");
    }


    @Override
    @Transactional
    public ResponseLessonDto updateLessonInfo(String courseId, String lessonId, LessonDto lessonDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of updating lesson with ID {} of course with ID {} started by teacher with ID {}(whose user ID is {})", lessonId, courseId, authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid());
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(Course.class));
        validateAccess(courseId, authenticatedTeacher);
        Lesson lesson = lessonRepository.findByUuidAndCourse(lessonId, course).orElseThrow(() -> new ResourceNotFoundException(Lesson.class));
        if (!lesson.getTeacher().getUuid().equals(authenticatedTeacher.getUuid())) {
            log.error("Teacher with ID {}(whose user ID is {}) is not allowed to update the lesson with ID {}", authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid(), lessonId);
            throw new ForbiddenException("NOT_ALLOWED_UPDATE_LESSON");
        }
        modelMapper.map(lessonDto, lesson);
        ResponseLessonDto updatedLesson = modelMapper.map(lesson, ResponseLessonDto.class);
        log.info("Lesson info successfully updated");
        return updatedLesson;
    }

    @Override
    public void deleteCourse(String courseId) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of deleting course with ID {} started by teacher with ID {}(whose user ID is {}", courseId, authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid());
        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.DELETE_COURSE)) {
            log.error("Authenticated teacher with ID {}(whose user ID is {}) doesn't have permission to delete course with ID {}", authenticatedTeacher.getUuid(), authenticatedTeacher.getUser().getUuid(), courseId);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        courseRepository.deleteById(courseId);
    }


    public List<Course> findCourseBy(String keyword) {
        return courseRepository.searchCoursesByKeyword(keyword);
    }

    private TeacherCourse validateAccess(String courseId, Teacher authenticatedTeacher) {
        return teacherCourseRepository.findByCourseUuidAndTeacher(courseId, authenticatedTeacher).orElseThrow(() -> {
            log.error("TeacherCourse relation is not present: That means teacher with ID {} is not teacher in course with ID {} or curse doesn't exit", authenticatedTeacher.getUuid(), courseId);
            return new ForbiddenException("NOT_ALLOWED");
        });
    }
}
