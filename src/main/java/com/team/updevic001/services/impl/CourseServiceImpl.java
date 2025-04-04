package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CategoryMapper;
import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.configuration.mappers.TeacherMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceAlreadyExistException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.*;
import com.team.updevic001.services.interfaces.CourseService;
import com.team.updevic001.services.interfaces.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final TeacherRepository teacherRepository;
    private final UserRoleRepository userRoleRepository;
    private final TeacherService teacherServiceImpl;
    private final ModelMapper modelMapper;
    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherMapper teacherMapper;

    @Override
    @Transactional
    public ResponseCourseDto createCourse(CourseDto courseDto) {
        Teacher teacher = teacherServiceImpl.getAuthenticatedTeacher();
        log.info("Creating a new teacher course. Teacher ID: {}, Course Title: {}", teacher.getId(), courseDto.getTitle());

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


    @Override
    @Transactional
    public ResponseTeacherWithCourses addTeacherToCourse(String courseId, String userId) {
        Teacher authenticatedTeacher = teacherServiceImpl.getAuthenticatedTeacher();
        log.info("Operation of adding new teacher with user ID {} to course with ID {} started by user with ID {}(whose teacher ID is {}", userId, courseId, authenticatedTeacher.getUser().getId(), authenticatedTeacher.getId());
        Course course = findCourseById(courseId);
        Teacher newTeacher = teacherRepository.findTeacherByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("TEACHER_NOT_FOUND"));

        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);

        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.ADD_TEACHER)) {
            log.error("Teacher with ID {} doesn't have permission to add new teacher", authenticatedTeacher.getId());
            throw new ForbiddenException("NOT_ALLOWED");
        }

        if (course.getTeacherCourses().stream().anyMatch(teacherCourse1 -> teacherCourse1.getTeacher().getId().equals(newTeacher.getId()))) {
            log.info("Teacher with ID {} is already teacher in course with ID {}", newTeacher.getId(), courseId);
            throw new ResourceAlreadyExistException("TEACHER_ALREADY_EXISTS_IN_THIS_COURSE");
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
        Teacher authenticatedTeacher = teacherServiceImpl.getAuthenticatedTeacher();
        log.info("Operation of updating course with ID {} started by user with ID {}(whose teacher ID is {}", courseId, authenticatedTeacher.getUser().getId(), authenticatedTeacher.getId());
        Course course = findCourseById(courseId);


        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        modelMapper.map(courseDto, course);

        courseRepository.save(course);
        teacherCourse.setCourse(course);
        teacherCourseRepository.save(teacherCourse);

        log.info("Teacher course updated successfully. Course ID: {}", course.getId());
        return courseMapper.courseDto(course);
    }


    @Override
    public void deleteCourse(String courseId) {
        Teacher authenticatedTeacher = teacherServiceImpl.getAuthenticatedTeacher();
        log.info("Operation of deleting course with ID {} started by teacher with ID {}(whose user ID is {}", courseId, authenticatedTeacher.getId(), authenticatedTeacher.getUser().getId());
        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.DELETE_COURSE)) {
            log.error("Authenticated teacher with ID {}(whose user ID is {}) doesn't have permission to delete course with ID {}", authenticatedTeacher.getId(), authenticatedTeacher.getUser().getId(), courseId);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        courseRepository.deleteById(courseId);
    }


    public List<Course> findCourseBy(String keyword) {
        return courseRepository.searchCoursesByKeyword(keyword);
    }

    public TeacherCourse validateAccess(String courseId, Teacher authenticatedTeacher) {
        return teacherCourseRepository.findByCourseIdAndTeacher(courseId, authenticatedTeacher).orElseThrow(() -> {
            log.error("TeacherCourse relation is not present: That means teacher with ID {} is not teacher in course with ID {} or curse doesn't exit", authenticatedTeacher.getId(), courseId);
            return new ForbiddenException("NOT_ALLOWED");
        });
    }


    public Course findCourseById(String courseId) {
        log.debug("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found with ID: {}", courseId);
                    return new ResourceNotFoundException("COURSE_NOT_FOUND");
                });
    }

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

}
