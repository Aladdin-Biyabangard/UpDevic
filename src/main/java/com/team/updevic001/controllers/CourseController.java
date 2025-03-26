package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.enums.CourseCategoryType;
import com.team.updevic001.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseServiceImpl;

    @GetMapping(path = "search")
    public ResponseEntity<List<ResponseCourseDto>> searchCourse(@RequestParam String keyword) {
        List<ResponseCourseDto> course = courseServiceImpl.searchCourse(keyword);
        return ResponseEntity.ok(course);
    }


    @GetMapping(path = "/{courseId}/")
    public ResponseEntity<ResponseCourseLessonDto> getCourse(@PathVariable String courseId) {
        ResponseCourseLessonDto course = courseServiceImpl.getCourse(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "all")
    public ResponseEntity<List<ResponseCourseDto>> getCourses() {
        List<ResponseCourseDto> courses = courseServiceImpl.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping(path = "category")
    public ResponseEntity<List<ResponseCategoryDto>> getCategory(@RequestParam CourseCategoryType categoryType) {
        List<ResponseCategoryDto> category = courseServiceImpl.getCategory(categoryType);
        return ResponseEntity.ok(category);
    }

    @GetMapping(path = "comment/{courseId}")
    public ResponseEntity<List<ResponseCommentDto>> getCourseComment(@PathVariable String courseId) {
        List<ResponseCommentDto> courseComment = courseServiceImpl.getCourseComment(courseId);
        return ResponseEntity.ok(courseComment);
    }

    @DeleteMapping(path = "/{courseId}/")
    public ResponseEntity<String> deleteCourseComment(@PathVariable String courseId,
                                                      @RequestParam String commentId) {
        courseServiceImpl.deleteCourseComment(courseId, commentId);
        return ResponseEntity.ok("Comment successfully deleted!");
    }

}
