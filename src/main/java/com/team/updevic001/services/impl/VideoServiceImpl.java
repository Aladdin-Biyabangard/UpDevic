package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import com.team.updevic001.services.interfaces.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final LessonServiceImpl lessonServiceImpl;
    @Value("${video.directory}")
    private String VIDEO_DIRECTORY;

    @Override
    public String uploadVideo(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            log.warn("An empty file was attempted to be uploaded!");
            throw new IllegalArgumentException("FILE_IS_EMPTY");
        }

        File dir = new File(VIDEO_DIRECTORY);
        if (!dir.exists() && !dir.mkdirs()) {
            log.error("Failed to create video directory!");
            throw new IOException("VIDEO_DIRECTORY_CREATION_FAILED");
        }

        // Faylın adını təhlükəsiz şəkildə əldə edirik
        String fileName = Objects.requireNonNull(file.getOriginalFilename());

        // Faylın tam yolunu qururuq
        Path filePath = Paths.get(VIDEO_DIRECTORY, fileName);
        File dest = filePath.toFile();

        try {
            file.transferTo(dest);
            log.info("Video successfully uploaded: {}", filePath);
            return fileName; // Yalnız fayl adını qaytarırıq
        } catch (IOException e) {
            log.error("Error while uploading video: {}", e.getMessage());
            throw new Exception("VIDEO_UPLOAD_FAILED", e);
        }
    }


    @Override
    public LessonVideoResponse getVideo(String lessonId, String videoName) throws MalformedURLException {
        String filePath = VIDEO_DIRECTORY + videoName;
        File videoFile = new File(filePath);

        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);

        if (!lesson.getVideoUrl().equalsIgnoreCase(filePath)) {
            throw new IllegalArgumentException("There is no video in the lesson.");
        }
        if (!videoFile.exists()) {
            log.warn("Requested a non-existing video: {}", videoName);
            throw new ResourceNotFoundException("VIDEO_NOT_FOUND");
        }

        Path path = Paths.get(videoFile.getAbsolutePath());
        log.info("Video loaded successfully: {}", path);

        Resource videoResource = new UrlResource(path.toUri());

        return new LessonVideoResponse(
                lesson.getTitle(),
                lesson.getDescription(),
                videoResource
        );
    }


}

