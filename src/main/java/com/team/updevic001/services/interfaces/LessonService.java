package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LessonService {

    ResponseLessonDto assignLessonToCourse(String courseId, LessonDto lessonDto, MultipartFile file) throws Exception;

    ResponseLessonDto updateLessonInfo(String lessonId, LessonDto lessonDto);

    List<ResponseLessonShortInfoDto> getShortLessonsByCourse(String courseId);

    ResponseLessonDto getFullLessonByLessonId(String lessonId);


    List<ResponseLessonDto> getTeacherLessons();


    LessonVideoResponse getVideo(String lessonId);


    Lesson findLessonById(String lessonId);

    void deleteLesson(String lessonId);

    void deleteTeacherLessons();


}
