package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.services.interfaces.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherServiceImpl;


    @Operation(summary = "Teacher creates a new course.")
    @PostMapping(path = "{userId}/course/create")
    public ResponseEntity<ResponseCourseDto> createCourse(@PathVariable String userId,
                                                          @RequestBody CourseDto courseDto) {
        ResponseCourseDto teacherCourse = teacherServiceImpl.createCourse(userId, courseDto);
        return ResponseEntity.ok(teacherCourse);
    }

    @Operation(summary = "A new teacher is added to the course.")
    @PostMapping(path = "add/teacher/course")
    public ResponseEntity<ResponseTeacherWithCourses> addTeacherToCourse(@RequestParam String teacher,
                                                                         @RequestParam String course) {
        ResponseTeacherWithCourses responseTeacherWithCourses = teacherServiceImpl.addTeacherToCourse(teacher, course);
        return ResponseEntity.ok(responseTeacherWithCourses);
    }

    @Operation(summary = "The teacher adds a new lesson to the course.")
    @PostMapping(path = "course/{courseId}/lesson/assign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseLessonShortInfoDto> assignLessonToCourse(
            @PathVariable String courseId,
            @ModelAttribute("lesson") LessonDto lessonDto,
            @RequestPart(value = "file", required = false) final MultipartFile file) throws Exception {
        ResponseLessonShortInfoDto responseLesson = teacherServiceImpl.assignLessonToCourse(courseId, lessonDto, file);
        return ResponseEntity.ok(responseLesson);
    }


    @Operation(summary = "Update course information for a teacher.")
    @PutMapping(path = "course/{courseId}/update")
    public ResponseEntity<Void> updateTeacherCourse(@PathVariable String courseId,
                                                    @RequestBody CourseDto courseDto) {
        teacherServiceImpl.updateTeacherCourseInfo(courseId, courseDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update teacher-related lesson information")
    @PutMapping(path = "lesson/{lessonId}/update")
    public ResponseEntity<Void> updateTeacherLesson(@PathVariable String lessonId,
                                                    @RequestBody LessonDto lessonDto) {
        teacherServiceImpl.updateTeacherLessonInfo(lessonId, lessonDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "View teacher-related course.")
    @GetMapping(path = "course/{courseId}/info")
    public ResponseEntity<ResponseCourseShortInfoDto> getTeacherCourse(@PathVariable String courseId) {
        ResponseCourseShortInfoDto teacherCourse = teacherServiceImpl.getTeacherCourse(courseId);
        return ResponseEntity.ok(teacherCourse);
    }

    @Operation(summary = "View the teacher courses.")
    @GetMapping(path = "user-courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getTeacherAndCourses() {
        List<ResponseCourseShortInfoDto> teacherAndCourses = teacherServiceImpl.getTeacherAndRelatedCourses();
        return ResponseEntity.ok(teacherAndCourses);
    }

    @Operation(summary = "Getting teacher lesson information")
    @GetMapping(path = "lesson/{lessonId}")
    public ResponseEntity<ResponseLessonDto> getTeacherLesson(@PathVariable String lessonId) {
        ResponseLessonDto teacherLesson = teacherServiceImpl.getTeacherLesson(lessonId);
        return ResponseEntity.ok(teacherLesson);
    }

    @Operation(summary = "All lessons of the teacher-related course")
    @GetMapping(path = "course/{courseId}/lessons")
    public ResponseEntity<List<ResponseLessonDto>> getLessonsByCourse(@PathVariable String courseId) {
        List<ResponseLessonDto> teacherLessonsByCourse = teacherServiceImpl.getTeacherLessonsByCourse(courseId);
        return ResponseEntity.ok(teacherLessonsByCourse);
    }

    @Operation(summary = "See all of the teacher's lessons")
    @GetMapping(path = "teacher-lessons")
    public ResponseEntity<List<ResponseLessonDto>> getTeacherLessons() {
        List<ResponseLessonDto> teacherLessons = teacherServiceImpl.getTeacherLessons();
        return ResponseEntity.ok(teacherLessons);
    }

    @Operation(summary = "Delete the teacher's course")
    @DeleteMapping(path = "{userId}/course/{courseId}/delete")
    public ResponseEntity<Void> deleteTeacherCourse(@PathVariable String userId,
                                                    @PathVariable String courseId) {
        teacherServiceImpl.deleteTeacherCourse(userId, courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete the teacher's lesson")
    @DeleteMapping(path = "{userId}/lesson/{lessonId}/delete")
    public ResponseEntity<Void> deleteTeacherLesson(@PathVariable String userId,
                                                    @PathVariable String lessonId) {
        teacherServiceImpl.deleteTeacherLesson(userId, lessonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete the teacher's courses")
    @DeleteMapping(path = "{userId}/courses/delete")
    public ResponseEntity<Void> deleteTeacherCourses(@PathVariable String userId) {
        teacherServiceImpl.deleteTeacherCourses(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete the teacher's lessons")
    @DeleteMapping(path = "{userId}/lessons/delete")
    public ResponseEntity<Void> deleteTeacherLessons(@PathVariable String userId) {
        teacherServiceImpl.deleteTeacherLessons(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete the teacher")
    @DeleteMapping(path = "delete/{userId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String userId) {
        teacherServiceImpl.deleteTeacher(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Bütün müəllimləri silmək
    @DeleteMapping(path = "delete/all")
    public ResponseEntity<Void> deleteAllTeachers() {
        teacherServiceImpl.deleteAllTeachers();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}