package com.team.updevic001.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    String uploadVideo(MultipartFile file) throws Exception;

}
