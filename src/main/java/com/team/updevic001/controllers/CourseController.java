package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.services.interfaces.CourseService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Teacher creates a new course.")
    @PostMapping(path = "course/create")
    public ResponseEntity<ResponseCourseDto> createCourse(@RequestBody CourseDto courseDto) {
        ResponseCourseDto teacherCourse = courseService.createCourse(courseDto);
        return ResponseEntity.ok(teacherCourse);
    }

    @Operation(summary = "A new teacher is added to the course.")
    @PostMapping(path = "add/teacher/course")
    public ResponseEntity<ResponseTeacherWithCourses> addTeacherToCourse(@RequestParam String teacher,
                                                                         @RequestParam String course) {
        ResponseTeacherWithCourses responseTeacherWithCourses = courseService.addTeacherToCourse(teacher, course);
        return ResponseEntity.ok(responseTeacherWithCourses);
    }

    @Operation(summary = "Update course information for a teacher.")
    @PutMapping(path = "course/{courseId}/update")
    public ResponseEntity<Void> updateTeacherCourse(@PathVariable String courseId,
                                                    @RequestBody CourseDto courseDto) {
        courseService.updateTeacherCourseInfo(courseId, courseDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Search for courses by keyword")
    @GetMapping(path = "search")
    public ResponseEntity<List<ResponseCourseDto>> searchCourse(@RequestParam String keyword) {
        List<ResponseCourseDto> course = courseService.searchCourse(keyword);
        return ResponseEntity.ok(course);
    }


    @GetMapping(path = "/{courseId}/")
    public ResponseEntity<ResponseCourseLessonDto> getCourse(@PathVariable String courseId) {
        ResponseCourseLessonDto course = courseService.getCourse(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "all")
    public ResponseEntity<List<ResponseCourseDto>> getCourses() {
        List<ResponseCourseDto> courses = courseService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping(path = "category")
    public ResponseEntity<List<ResponseCategoryDto>> getCategory(@RequestParam CourseCategoryType categoryType) {
        List<ResponseCategoryDto> category = courseService.getCategory(categoryType);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Delete the teacher's course")
    @DeleteMapping(path = "{courseId}/course/delete")
    public ResponseEntity<Void> deleteTeacherCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
