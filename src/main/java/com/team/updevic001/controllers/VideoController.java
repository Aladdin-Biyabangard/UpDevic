package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import com.team.updevic001.services.interfaces.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

@Slf4j
@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoServiceImpl;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(
            @RequestPart("file") final MultipartFile file) throws Exception {
        String result = videoServiceImpl.uploadVideo(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{lessonId}/video/{videoName}")
    public ResponseEntity<LessonVideoResponse> getLessonWithVideo(
            @PathVariable String lessonId,
            @PathVariable String videoName) throws MalformedURLException {

        LessonVideoResponse response = videoServiceImpl.getVideo(lessonId, videoName);
        return ResponseEntity.ok(response);
    }
}
