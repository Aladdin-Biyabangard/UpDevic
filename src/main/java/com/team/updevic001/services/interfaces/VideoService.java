package com.team.updevic001.services.interfaces;

import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface VideoService {

    String uploadVideo(MultipartFile file) throws Exception;

    LessonVideoResponse getVideo(String lessonId, String videoName) throws MalformedURLException;
}
