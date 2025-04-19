package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.TeacherCourse;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.dao.repositories.TeacherCourseRepository;
import com.team.updevic001.dao.repositories.TeacherRepository;
import com.team.updevic001.exceptions.AlreadyExistsException;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.model.enums.TeacherPermission;
import com.team.updevic001.model.enums.TeacherPrivileges;
import com.team.updevic001.services.interfaces.CourseService;
import com.team.updevic001.services.interfaces.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final ModelMapper modelMapper;
    private final TeacherCourseRepository teacherCourseRepository;

    @Override
    @Transactional
    public ResponseCourseDto createCourse(CourseDto courseDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of creating new course started by user with ID {}(whose teacher ID is {}", authenticatedTeacher.getUser().getId(), authenticatedTeacher.getId());
        Course course = modelMapper.map(courseDto, Course.class);
        course.setHeadTeacher(authenticatedTeacher.getUser().getFirstName() + " " + authenticatedTeacher.getUser().getLastName());
        courseRepository.save(course);

        log.info("Course saved successfully. Course ID: {}", course.getId());

        TeacherCourse teacherCourse = TeacherCourse.builder()
                .teacher(authenticatedTeacher)
                .course(course)
                .teacherPrivilege(TeacherPrivileges.HEAD_TEACHER)
                .build();
        teacherCourseRepository.save(teacherCourse);
        log.info("TeacherCourse relationship saved successfully. Teacher ID: {}, Course ID: {}", authenticatedTeacher.getId(), course.getId());
        ResponseCourseDto responseCourseDto = courseMapper.courseDto(course);
        log.info("Operation of creating course ended successfully");
        return responseCourseDto;
    }


    @Override
    @Transactional
    public ResponseCourseDto addTeacherToCourse(String courseId, String userId) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of adding new teacher with user ID {} to course with ID {} started by user with ID {}(whose teacher ID is {}", userId, courseId, authenticatedTeacher.getUser().getId(), authenticatedTeacher.getId());
        Course course = findCourseById(courseId);
        Teacher newTeacher = teacherRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException(Teacher.class));

        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);

        if (!teacherCourse.getTeacherPrivilege().hasPermission(TeacherPermission.ADD_TEACHER)) {
            log.error("Teacher with ID {} doesn't have permission to add new teacher", authenticatedTeacher.getId());
            throw new ForbiddenException("NOT_ALLOWED");
        }

        if (course.getTeacherCourses().stream().anyMatch(teacherCourse1 -> teacherCourse1.getTeacher().getId().equals(newTeacher.getId()))) {
            log.info("Teacher with ID {} is already teacher in course with ID {}", newTeacher.getId(), courseId);
            throw new AlreadyExistsException("TEACHER_ALREADY_EXISTS_IN_THIS_COURSE");
        }

        TeacherCourse newTeacherCourseRelation = TeacherCourse.builder()
                .course(course)
                .teacher(newTeacher)
                .teacherPrivilege(TeacherPrivileges.ASSISTANT_TEACHER)
                .build();
        teacherCourseRepository.save(newTeacherCourseRelation);
        log.info("New teacher successfully added to course");
        return courseMapper.courseDto(course);
    }


    @Override
    @Transactional
    public ResponseCourseDto updateCourse(String courseId, CourseDto courseDto) {
        Teacher authenticatedTeacher = teacherService.getAuthenticatedTeacher();
        log.info("Operation of updating course with ID {} started by user with ID {}(whose teacher ID is {}", courseId, authenticatedTeacher.getUser().getId(), authenticatedTeacher.getId());
        Course findCourse = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

        TeacherCourse teacherCourse = validateAccess(courseId, authenticatedTeacher);
        modelMapper.map(courseDto, findCourse);

        courseRepository.save(findCourse);
        teacherCourse.setCourse(findCourse);
        teacherCourseRepository.save(teacherCourse);

        log.info("Teacher course updated successfully. Course ID: {}", findCourse.getId());
        return courseMapper.courseDto(findCourse);
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


    public TeacherCourse validateAccess(String courseId, Teacher authenticatedTeacher) {
        return teacherCourseRepository.findByCourseIdAndTeacher(courseId, authenticatedTeacher).orElseThrow(() -> {
            log.error("TeacherCourse relation is not present: That means teacher with ID {} is not teacher in course with ID {} or curse doesn't exit", authenticatedTeacher.getId(), courseId);
            return new ForbiddenException("NOT_ALLOWED");
        });
    }


    @Override
    @Cacheable(value = "courseSearchCache", key = "#keyword", unless = "#result == null", cacheManager = "cacheManager")
    public List<ResponseCourseShortInfoDto> searchCourse(String keyword) {
        List<Course> courses = courseRepository.findCourseByTitle(keyword);
        return !courses.isEmpty() ? courseMapper.toCourseResponse(courses) : List.of();
    }

    @Override
    @Cacheable(value = "courseSearchCache", key = "#courseId", unless = "#result==null", cacheManager = "cacheManager")
    public ResponseCourseLessonDto getCourse(String courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courseMapper.toFullResponse(course);
    }

    @Override
    public List<ResponseCourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return !courses.isEmpty() ? courseMapper.courseDto(courses) : List.of();
    }


    @Override
    public List<ResponseCategoryDto> getCategories() {
        return Arrays.stream(CourseCategoryType.values())
                .map(type -> {
                    List<Course> courses = courseRepository.findCourseByCourseCategoryType(type);
                    return new ResponseCategoryDto(type, courses.size());
                })
                .toList();
    }

    // TODO birinci getCategories işə düşərək categoriyal gətirir.
    //  Sonra ise findcursesByCategory metodu ile ordaki categoriyalara uygun qisa kurs melumatlari gelir.
    //  Sonra ise lesson service kecirik. Gələn kiçik kurs məlumatlarına baxdıqdan sonra hər hansınınsa üzərinə vurduqda
    //  lesson servicde olan findLessonBYCourse id metodu ise dusur ve cursun butun derslerinin qisa melumatlarini verir.
    //  Ondan sonra ise istenilen ders uzerine vurduqda istifadeciye lazimi butun melumatlar gedir .

    @Override
    public List<ResponseCourseShortInfoDto> findCoursesByCategory(CourseCategoryType categoryType) {
        List<Course> courseByCourseCategoryType = courseRepository.findCourseByCourseCategoryType(categoryType);
        return !courseByCourseCategoryType.isEmpty() ? courseMapper.toCourseResponse(courseByCourseCategoryType) : new ArrayList<>();
    }


    public Course findCourseById(String courseId) {
        log.debug("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found with ID: {}", courseId);
                    return new ResourceNotFoundException("COURSE_NOT_FOUND");
                });
    }

    @Scheduled(fixedRate = 300000) // 5 dəqiqə (300000 ms)
    @CacheEvict(value = "courseSearchCache", allEntries = true)
    public void clearCache() {
        System.out.println("Cache təmizləndi.");
    }

}
