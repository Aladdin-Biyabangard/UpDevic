package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherDto;
import com.team.updevic001.services.interfaces.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> deleteTeacher(@PathVariable String userId) {
        teacherServiceImpl.deleteTeacher(userId);
        return ResponseEntity.ok("Teacher deleted!");
    }

    @Operation(summary = "Delete all teachers")
    @DeleteMapping(path = "/all")
    public ResponseEntity<String> deleteAllTeachers() {
        teacherServiceImpl.deleteAllTeachers();
        return ResponseEntity.ok("All teacher deleted!");
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<ResponseTeacherDto>> searchTeacher(@RequestParam String keyword) {
        List<ResponseTeacherDto> responseTeachers = teacherServiceImpl.searchTeacher(keyword);
        return ResponseEntity.ok(responseTeachers);
    }
}