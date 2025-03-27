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
    public ResponseEntity<String> enrollInCourse(@RequestParam String courseId) {
        studentService.enrollInCourse(courseId);
        return ResponseEntity.ok("Student successfully enrolled in the course.");
    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<String> unenrollFromCourse(@RequestParam String courseId) {
        studentService.unenrollUserFromCourse(courseId);
        return ResponseEntity.ok("Student successfully unenrolled from the course.");
    }

    @GetMapping
    public ResponseEntity<ResponseCourseShortInfoDto> getStudentCourse(@RequestParam String courseId) {
        ResponseCourseShortInfoDto studentCourse = studentService.getStudentCourse(courseId);
        return ResponseEntity.ok(studentCourse);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getStudentCourses() {
        List<ResponseCourseShortInfoDto> courses = studentService.getStudentCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<ResponseCourseLessonDto>> getStudentLessons() {
        List<ResponseCourseLessonDto> lessons = studentService.getStudentLessons();
        return ResponseEntity.ok(lessons);
    }

    @DeleteMapping("/delete-course")
    public ResponseEntity<String> deleteStudentCourse(@RequestParam String courseId) {
        studentService.deleteStudentCourse(courseId);
        return ResponseEntity.ok("Student's course enrollment successfully deleted.");
    }

}
