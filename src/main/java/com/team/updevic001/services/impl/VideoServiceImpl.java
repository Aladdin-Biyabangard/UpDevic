package com.team.updevic001.services.impl;

import com.team.updevic001.services.interfaces.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

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

}

