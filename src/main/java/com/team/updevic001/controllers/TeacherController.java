package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
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

    private final TeacherService teacherServiceImpl;

    @Operation(summary = "View the teacher courses.")
    @GetMapping(path = "/{teacherId}/courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getTeacherAndCourses(@PathVariable String teacherId) {
        List<ResponseCourseShortInfoDto> teacherAndCourses = teacherServiceImpl.getTeacherAndRelatedCourses(teacherId);
        return ResponseEntity.ok(teacherAndCourses);
    }

    @Operation(summary = "Delete the teacher")
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String userId) {
        teacherServiceImpl.deleteTeacher(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete all teachers")
    @DeleteMapping(path = "/all")
    public ResponseEntity<Void> deleteAllTeachers() {
        teacherServiceImpl.deleteAllTeachers();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}