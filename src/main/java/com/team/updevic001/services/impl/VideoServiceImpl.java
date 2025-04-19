package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.services.interfaces.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mp4parser.IsoFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;


    @Override
    public String uploadVideo(MultipartFile multipartFile, String lessonId) throws IOException {
        String fileName = lessonId + "_" + ".mp4";

        if (!"video/mp4".equals(multipartFile.getContentType())) {
            throw new IllegalArgumentException("You can simply upload an mp4 file.");
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .contentType(multipartFile.getContentType())
                .build();
        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
        return getVideoUrl(fileName);
    }


    public String getVideoUrl(String lessonId) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(lessonId)
                .build();


        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build();
        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedGetObjectRequest.url().toString();
    }


    public void deleteVideoFromS3(String lessonId) {
        String key = lessonId + "_.mp4";
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        log.info("Video file {} deleted from S3", lessonId);
    }

    public void deleteVideosFromS3(List<Lesson> lessons) {
        lessons.forEach(lesson -> {
            String videoKey = lesson.getId(); // Video URL-i (key)
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(videoKey)
                    .build();
            try {
                s3Client.deleteObject(deleteObjectRequest);
                log.info("Video file {} deleted from S3", videoKey);
            } catch (Exception e) {
                log.error("Failed to delete video file {} from S3", videoKey, e);
            }
        });
    }


    public File convertToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("temp", null);
        file.transferTo(convFile);
        return convFile;
    }


    public String getVideoDurationInSeconds(File file) throws IOException {
        IsoFile isoFile = new IsoFile(file.getAbsolutePath());
        long duration = isoFile.getMovieBox().getMovieHeaderBox().getDuration();
        long timescale = isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
        isoFile.close();
        long seconds = duration / timescale;
        return formatVideoDuration(seconds);
    }

    private String formatVideoDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }


}

