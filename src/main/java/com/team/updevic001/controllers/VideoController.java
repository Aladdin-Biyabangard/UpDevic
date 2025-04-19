package com.team.updevic001.controllers;

import com.team.updevic001.services.interfaces.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoServiceImpl;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(
            @RequestPart("file") final MultipartFile file, String title) throws Exception {
        String result = videoServiceImpl.uploadVideo(file, title);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{title}")
    public ResponseEntity<String> getVideo(@PathVariable("title") String title) {
        String video = videoServiceImpl.getVideoUrl(title);
        return ResponseEntity.ok(video);
    }

}
