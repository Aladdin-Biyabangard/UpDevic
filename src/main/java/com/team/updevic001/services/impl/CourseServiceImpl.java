package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CategoryMapper;
import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.configuration.mappers.TeacherMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.model.enums.TeacherPermission;
import com.team.updevic001.services.interfaces.CourseService;
import com.team.updevic001.services.interfaces.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final TeacherService teacherService;
    private final UserRoleRepository userRoleRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherMapper teacherMapper;


    @Override
    public ResponseCourseDto createCourse(CourseDto courseDto) {
        Teacher teacher = teacherService.getAuthenticatedTeacher();
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
    public ResponseTeacherWithCourses addTeacherToCourse(String userId, String courseId) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        Teacher teacher = teacherService.findTeacherByUserId(userId);

        Course course = findCourseById(courseId);

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
    public void updateTeacherCourseInfo(String courseId, CourseDto courseDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Updating teacher course info. Teacher ID: {}, Course ID: {}", authenticatedTeacher.getId(), courseId);
        Course course = findCourseById(courseId);
        validateAccess(courseId, authenticatedTeacher);
        TeacherCourse teacherCourse = findTeacherCourse(course, authenticatedTeacher);

        modelMapper.map(courseDto, course);

        courseRepository.save(course);
        teacherCourse.setCourse(course);
        teacherCourseRepository.save(teacherCourse);
        log.info("Teacher course updated successfully. Course ID: {}", course.getId());
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

    @Override
    public List<Course> findCourseBy(String keyword) {
        return courseRepository.searchCoursesByKeyword(keyword);
    }

    @Override
    public Course findCourseById(String courseId) {
        log.debug("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found with ID: {}", courseId);
                    return new ResourceNotFoundException("COURSE_NOT_FOUND");
                });
    }


    @Override
    public TeacherCourse findTeacherCourse(Course course, Teacher teacher) {
        log.info("Finding teacher-course relationship. Teacher ID: {}, Course ID: {}", teacher.getId(), course.getId());
        return teacherCourseRepository.findByCourseAndTeacher(course, teacher).orElseThrow(() -> new ResourceNotFoundException("Teacher with ID: " + teacher.getId() + " is not assigned to course with ID: " + course.getId()));
    }

    @Override
    public void deleteCourse(String courseId) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of deleting course with ID {} started by teacher with ID {}(whose user ID is {}", courseId, authenticatedTeacher.getId(), authenticatedTeacher.getUser().getId());
        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.DELETE_COURSE)) {
            log.error("Authenticated teacher with ID {}(whose user ID is {}) doesn't have permission to delete course with ID {}", authenticatedTeacher.getId(), authenticatedTeacher.getUser().getId(), courseId);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        courseRepository.deleteById(courseId);
    }


    private TeacherCourse validateAccess(String courseId, Teacher authenticatedTeacher) {
        return teacherCourseRepository.findByCourseIdAndTeacher(courseId, authenticatedTeacher).orElseThrow(() -> {
            log.error("TeacherCourse relation is not present: That means teacher with ID {} is not teacher in course with ID {} or curse doesn't exit", authenticatedTeacher.getId(), courseId);
            return new ForbiddenException("NOT_ALLOWED");
        });
    }

}
