package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import com.team.updevic001.services.interfaces.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonServiceImpl;

    @PostMapping(path = "/{courseId}/lessons")
    public ResponseEntity<ResponseLessonDto> assignLessonToCourse(
            @PathVariable String courseId,
            @ModelAttribute("lesson") LessonDto lessonDto,
            @RequestPart(value = "file", required = false) final MultipartFile file) throws Exception {
        ResponseLessonDto responseLessonDto = lessonServiceImpl.assignLessonToCourse(courseId, lessonDto, file);
        return new ResponseEntity<>(responseLessonDto, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}/lessons/{lessonId}")
    public ResponseEntity<ResponseLessonDto> updateLessonInfo(@PathVariable String courseId,
                                                              @PathVariable String lessonId,
                                                              @Valid @RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonServiceImpl.updateLessonInfo(courseId, lessonId, lessonDto));
    }

    @GetMapping(path = "/{courseId}/lessons")
    public ResponseEntity<List<ResponseLessonDto>> getLessonsByCourse(@PathVariable String courseId) {
        List<ResponseLessonDto> teacherLessonsByCourse = lessonServiceImpl.getLessonsByCourse(courseId);
        return ResponseEntity.ok(teacherLessonsByCourse);
    }

    @Operation(summary = "See all of the teacher's lessons")
    @GetMapping(path = "teacher-lessons")
    public ResponseEntity<List<ResponseLessonDto>> getTeacherLessons() {
        List<ResponseLessonDto> teacherLessons = lessonServiceImpl.getTeacherLessons();
        return ResponseEntity.ok(teacherLessons);
    }


    //Bunu addelni API olaraq yox. getLessonsByCourse metounda olan REsponseLEssonDto icine elave edeceiyik

    @GetMapping("/{lessonId}/video/{videoName}")
    public ResponseEntity<LessonVideoResponse> getLessonWithVideo(
            @PathVariable String lessonId,
            @PathVariable String videoName) throws MalformedURLException {

        LessonVideoResponse response = lessonServiceImpl.getVideo(lessonId, videoName);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{courseId}/lesson/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable String courseId,
                                             @PathVariable String lessonId) {
        lessonServiceImpl.deleteLesson(courseId, lessonId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete the teacher's lessons")
    @DeleteMapping(path = "lessons/delete")
    public ResponseEntity<Void> deleteTeacherLessons() {
        lessonServiceImpl.deleteTeacherLessons();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
