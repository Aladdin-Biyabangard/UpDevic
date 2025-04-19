package com.team.updevic001.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {

    String uploadVideo(MultipartFile multipartFile, String title) throws IOException;

    String getVideoUrl(String key);

}
