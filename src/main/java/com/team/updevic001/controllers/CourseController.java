package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
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

    @GetMapping(path = "/search")
    public ResponseEntity<List<ResponseCourseDto>> searchCourse(@RequestParam String keyword) {
        List<ResponseCourseDto> course = courseService.searchCourse(keyword);
        return ResponseEntity.ok(course);
    }


    @GetMapping(path = "/{courseId}/")
    public ResponseEntity<ResponseCourseLessonDto> getCourse(@PathVariable String courseId) {
        ResponseCourseLessonDto course = courseService.getCourse(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ResponseCourseDto>> getCourses() {
        List<ResponseCourseDto> courses = courseService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping(path = "/category")
    public ResponseEntity<List<ResponseCategoryDto>> getCategory(@RequestParam CourseCategoryType categoryType) {
        List<ResponseCategoryDto> category = courseService.getCategory(categoryType);
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

    @DeleteMapping(path = "/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/{courseId}/teacher")
    public ResponseEntity<ResponseTeacherWithCourses> addTeacherToCourse(@PathVariable String courseId, @RequestParam String userId) {
        ResponseTeacherWithCourses responseTeacherWithCourses = courseService.addTeacherToCourse(courseId, userId);
        return ResponseEntity.ok(responseTeacherWithCourses);
    }

}

