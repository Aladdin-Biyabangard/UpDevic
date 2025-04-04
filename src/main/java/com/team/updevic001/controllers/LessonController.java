package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.services.interfaces.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonServiceImpl;

    @Operation(summary = "The teacher adds a new lesson to the course.")
    @PostMapping(path = "course/{courseId}/lesson/assign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseLessonShortInfoDto> assignLessonToCourse(
            @PathVariable String courseId,
            @ModelAttribute("lesson") LessonDto lessonDto,
            @RequestPart(value = "file", required = false) final MultipartFile file) throws Exception {
        ResponseLessonShortInfoDto responseLesson = lessonServiceImpl.assignLessonToCourse(courseId, lessonDto, file);
        return ResponseEntity.ok(responseLesson);
    }


    @Operation(summary = "Update teacher-related lesson information")
    @PutMapping(path = "lesson/{lessonId}/update")
    public ResponseEntity<Void> updateTeacherLesson(@PathVariable String lessonId,
                                                    @RequestBody LessonDto lessonDto) {
        lessonServiceImpl.updateTeacherLessonInfo(lessonId, lessonDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "{lessonId}/watch")
    public ResponseEntity<ResponseLessonDto> getLessonById(@PathVariable String lessonId) {
        ResponseLessonDto lesson = lessonServiceImpl.getLessonById(lessonId);
        return ResponseEntity.ok(lesson);
    }

}
