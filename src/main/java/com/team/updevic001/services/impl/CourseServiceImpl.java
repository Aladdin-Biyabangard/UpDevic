package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CategoryMapper;
import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.CourseCategory;
import com.team.updevic001.dao.repositories.CourseCategoryRepository;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.services.interfaces.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


}
