package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.services.interfaces.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Enroll a student in a course")
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollInCourse(@RequestParam String courseId, @RequestParam String userId) {
        studentService.enrollInCourse(courseId, userId);
        return ResponseEntity.ok("Student successfully enrolled in the course.");
    }

    @Operation(summary = "Unenroll a student from a course")
    @DeleteMapping("/unenroll")
    public ResponseEntity<String> unenrollFromCourse(@RequestParam String userId,
                                                     @RequestParam String courseId) {
        studentService.unenrollUserFromCourse(userId, courseId);
        return ResponseEntity.ok("Student successfully unenrolled from the course.");
    }

    @Operation(summary = "Get a student's course information")
    @GetMapping
    public ResponseEntity<ResponseCourseShortInfoDto> getStudentCourse(@RequestParam String userId, @RequestParam String courseId) {
        ResponseCourseShortInfoDto studentCourse = studentService.getStudentCourse(userId, courseId);
        return ResponseEntity.ok(studentCourse);
    }

    @Operation(summary = "Get all courses of a student")
    @GetMapping("/courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getStudentCourses(@RequestParam String userId) {
        List<ResponseCourseShortInfoDto> courses = studentService.getStudentCourses(userId);
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Get all lessons of a student")
    @GetMapping("/lessons")
    public ResponseEntity<List<ResponseCourseLessonDto>> getStudentLessons(@RequestParam String userId) {
        List<ResponseCourseLessonDto> lessons = studentService.getStudentLessons(userId);
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Delete a student's comment on a course")
    @DeleteMapping("/delete-course-comment")
    public ResponseEntity<String> deleteStudentCourseComment(@RequestParam String userId,
                                                             @RequestParam String courseId,
                                                             @RequestParam String commentId) {
        studentService.deleteStudentCourseComment(userId, courseId, commentId);
        return ResponseEntity.ok("Student's comment on the course successfully deleted.");
    }

    @Operation(summary = "Delete a student's comment on a lesson")
    @DeleteMapping("/delete-lesson-comment")
    public ResponseEntity<String> deleteStudentLessonComment(@RequestParam String userId,
                                                             @RequestParam String lessonId,
                                                             @RequestParam String commentId) {
        studentService.deleteStudentLessonComment(userId, lessonId, commentId);
        return ResponseEntity.ok("Student's comment on the lesson successfully deleted.");
    }

    @Operation(summary = "API request to become a teacher!")
    @GetMapping(path = "/for-teacher")
    public ResponseEntity<String> requestToBecameTeacher() {
        return ResponseEntity.ok("https://forms.gle/GersS1t7jwena3Dz9");
    }
}
