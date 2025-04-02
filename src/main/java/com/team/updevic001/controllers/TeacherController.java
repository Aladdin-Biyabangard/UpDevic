package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherServiceImpl;

    @GetMapping(path = "/{teacherId}/courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getTeacherAndCourses(@PathVariable String teacherId) {
        List<ResponseCourseShortInfoDto> teacherAndCourses = teacherServiceImpl.getTeacherAndRelatedCourses(teacherId);
        return ResponseEntity.ok(teacherAndCourses);
    }

    // Müəllimi silmək
    @DeleteMapping(path = "/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String teacherId) {
        teacherServiceImpl.deleteTeacher(teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Bütün müəllimləri silmək
    @DeleteMapping(path = "/delete/all")
    public ResponseEntity<Void> deleteAllTeachers() {
        teacherServiceImpl.deleteAllTeachers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}