package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.services.interfaces.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "View teacher-related course.")
    @GetMapping(path = "course/{courseId}/info")
    public ResponseEntity<ResponseCourseShortInfoDto> getTeacherCourse(@PathVariable String courseId) {
        ResponseCourseShortInfoDto teacherCourse = teacherService.getTeacherCourse(courseId);
        return ResponseEntity.ok(teacherCourse);
    }

    @Operation(summary = "View the teacher courses.")
    @GetMapping(path = "user-courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getTeacherAndCourses() {
        List<ResponseCourseShortInfoDto> teacherAndCourses = teacherService.getTeacherAndRelatedCourses();
        return ResponseEntity.ok(teacherAndCourses);
    }

    @Operation(summary = "Getting teacher lesson information")
    @GetMapping(path = "lesson/{lessonId}")
    public ResponseEntity<ResponseLessonDto> getTeacherLesson(@PathVariable String lessonId) {
        ResponseLessonDto teacherLesson = teacherService.getTeacherLesson(lessonId);
        return ResponseEntity.ok(teacherLesson);
    }

    @Operation(summary = "All lessons of the teacher-related course")
    @GetMapping(path = "course/{courseId}/lessons")
    public ResponseEntity<List<ResponseLessonDto>> getLessonsByCourse(@PathVariable String courseId) {
        List<ResponseLessonDto> teacherLessonsByCourse = teacherService.getTeacherLessonsByCourse(courseId);
        return ResponseEntity.ok(teacherLessonsByCourse);
    }

    @Operation(summary = "See all of the teacher's lessons")
    @GetMapping(path = "teacher-lessons")
    public ResponseEntity<List<ResponseLessonDto>> getTeacherLessons() {
        List<ResponseLessonDto> teacherLessons = teacherService.getTeacherLessons();
        return ResponseEntity.ok(teacherLessons);
    }


    @Operation(summary = "Delete the teacher's lesson")
    @DeleteMapping(path = "{userId}/lesson/{lessonId}/delete")
    public ResponseEntity<Void> deleteTeacherLesson(@PathVariable String userId,
                                                    @PathVariable String lessonId) {
        teacherService.deleteTeacherLesson(userId, lessonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete the teacher's lessons")
    @DeleteMapping(path = "{userId}/lessons/delete")
    public ResponseEntity<Void> deleteTeacherLessons(@PathVariable String userId) {
        teacherService.deleteTeacherLessons(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete the teacher")
    @DeleteMapping(path = "delete/{userId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String userId) {
        teacherService.deleteTeacher(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Bütün müəllimləri silmək
    @DeleteMapping(path = "delete/all")
    public ResponseEntity<Void> deleteAllTeachers() {
        teacherService.deleteAllTeachers();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}