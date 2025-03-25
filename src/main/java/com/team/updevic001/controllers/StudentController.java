package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollInCourse(@RequestParam String courseId, @RequestParam String userId) {
        studentService.enrollInCourse(courseId, userId);
        return ResponseEntity.ok("Student successfully enrolled in the course.");
    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<String> unenrollFromCourse(@RequestParam String userId,
                                                     @RequestParam String courseId) {
        studentService.unenrollUserFromCourse(userId, courseId);
        return ResponseEntity.ok("Student successfully unenrolled from the course.");
    }

    @GetMapping
    public ResponseEntity<ResponseCourseShortInfoDto> getStudentCourse(@RequestParam String userId, @RequestParam String courseId) {
        ResponseCourseShortInfoDto studentCourse = studentService.getStudentCourse(userId, courseId);
        return ResponseEntity.ok(studentCourse);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getStudentCourses(@RequestParam String userId) {
        List<ResponseCourseShortInfoDto> courses = studentService.getStudentCourses(userId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<ResponseCourseLessonDto>> getStudentLessons(@RequestParam String userId) {
        List<ResponseCourseLessonDto> lessons = studentService.getStudentLessons(userId);
        return ResponseEntity.ok(lessons);
    }

    @DeleteMapping("/delete-course")
    public ResponseEntity<String> deleteStudentCourse(@RequestParam String userId,
                                                      @RequestParam String courseId) {
        studentService.deleteStudentCourse(userId, courseId);
        return ResponseEntity.ok("Student's course enrollment successfully deleted.");
    }

    @DeleteMapping("/delete-course-comment")
    public ResponseEntity<String> deleteStudentCourseComment(@RequestParam String userId,
                                                             @RequestParam String courseId,
                                                             @RequestParam String commentId) {
        studentService.deleteStudentCourseComment(userId, courseId, commentId);
        return ResponseEntity.ok("Student's comment on the course successfully deleted.");
    }

    @DeleteMapping("/delete-lesson-comment")
    public ResponseEntity<String> deleteStudentLessonComment(@RequestParam String userId,
                                                             @RequestParam String lessonId,
                                                             @RequestParam String commentId) {
        studentService.deleteStudentLessonComment(userId, lessonId, commentId);
        return ResponseEntity.ok("Student's comment on the lesson successfully deleted.");
    }
}
