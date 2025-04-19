package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import com.team.updevic001.services.interfaces.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonServiceImpl;

    @PostMapping(path = "/{courseId}/lessons", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseLessonDto> assignLessonToCourse(
            @PathVariable String courseId,
            @ModelAttribute("lesson") LessonDto lessonDto,
            @RequestPart("file") final MultipartFile file) throws Exception {
        ResponseLessonDto responseLessonDto = lessonServiceImpl.assignLessonToCourse(courseId, lessonDto, file);
        return new ResponseEntity<>(responseLessonDto, HttpStatus.CREATED);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<ResponseLessonDto> updateLessonInfo(@PathVariable String lessonId,
                                                              @Valid @RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonServiceImpl.updateLessonInfo(lessonId, lessonDto));
    }

    @GetMapping(path = "/{courseId}/lesson-short")
    public ResponseEntity<List<ResponseLessonShortInfoDto>> getLessonsByCourse(@PathVariable String courseId) {
        List<ResponseLessonShortInfoDto> teacherLessonsByCourse = lessonServiceImpl.getShortLessonsByCourse(courseId);
        return ResponseEntity.ok(teacherLessonsByCourse);
    }

    @GetMapping(path = "{lessonId}/lesson")
    public ResponseEntity<ResponseLessonDto> getFullLessonByLessonId(@PathVariable String lessonId) {
        ResponseLessonDto fullLessonByLessonId = lessonServiceImpl.getFullLessonByLessonId(lessonId);
        return ResponseEntity.ok(fullLessonByLessonId);
    }


    @Operation(summary = "See all of the teacher's lessons")
    @GetMapping(path = "teacher-lessons")
    public ResponseEntity<List<ResponseLessonDto>> getTeacherLessons() {
        List<ResponseLessonDto> teacherLessons = lessonServiceImpl.getTeacherLessons();
        return ResponseEntity.ok(teacherLessons);
    }


    //Bunu addelni API olaraq yox. getLessonsByCourse metounda olan REsponseLEssonDto icine elave edeceiyik

    @GetMapping("/{lessonId}/video")
    public ResponseEntity<LessonVideoResponse> getLessonWithVideo(
            @PathVariable String lessonId) {

        LessonVideoResponse response = lessonServiceImpl.getVideo(lessonId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable String lessonId) {
        lessonServiceImpl.deleteLesson(lessonId);
        return ResponseEntity.ok("Lesson deleted successfully!");
    }

    @Operation(summary = "Delete the teacher's lessons")
    @DeleteMapping(path = "lessons/delete")
    public ResponseEntity<String> deleteTeacherLessons() {
        lessonServiceImpl.deleteTeacherLessons();
        return ResponseEntity.ok("All lessons deleted!");
    }
}
