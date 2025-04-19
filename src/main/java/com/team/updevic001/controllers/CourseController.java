package com.team.updevic001.controllers;

import com.team.updevic001.configuration.config.syncrn.RateLimit;
import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.services.interfaces.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @RateLimit
    @GetMapping(path = "/search")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> searchCourse(@RequestParam String keyword) {
        List<ResponseCourseShortInfoDto> courses = courseService.searchCourse(keyword);
        return ResponseEntity.ok(courses);
    }


    @GetMapping(path = "/{courseId}/full")
    public ResponseEntity<ResponseCourseLessonDto> getCourse(@PathVariable String courseId) {
        ResponseCourseLessonDto course = courseService.getCourse(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ResponseCourseDto>> getCourses() {
        List<ResponseCourseDto> courses = courseService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping(path = "categories")
    public ResponseEntity<List<ResponseCategoryDto>> getCategories() {
        List<ResponseCategoryDto> categories = courseService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping(path = "/short")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> findCoursesByCategory(@RequestParam CourseCategoryType categoryType) {
        List<ResponseCourseShortInfoDto> category = courseService.findCoursesByCategory(categoryType);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<ResponseCourseDto> createCourse(@RequestBody CourseDto courseDto) {
        ResponseCourseDto teacherCourse = courseService.createCourse(courseDto);
        return new ResponseEntity<>(teacherCourse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{courseId}")
    public ResponseEntity<ResponseCourseDto> updateTeacherCourse(@PathVariable String courseId, @RequestBody CourseDto courseDto) {
        return new ResponseEntity<>(courseService.updateCourse(courseId, courseDto), HttpStatus.OK);
    }

    @PostMapping(path = "/{courseId}/teacher")
    public ResponseEntity<ResponseCourseDto> addTeacherToCourse(@PathVariable String courseId, @RequestParam String userId) {
        ResponseCourseDto responseTeacherWithCourses = courseService.addTeacherToCourse(courseId, userId);
        return ResponseEntity.ok(responseTeacherWithCourses);
    }

    @DeleteMapping(path = "/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

