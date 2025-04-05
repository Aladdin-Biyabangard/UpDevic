package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.video.LessonVideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface LessonService {

    ResponseLessonDto assignLessonToCourse(String courseId, LessonDto lessonDto, MultipartFile file) throws Exception;

    ResponseLessonDto updateLessonInfo(String lessonId, LessonDto lessonDto);

    List<ResponseLessonDto> getLessonsByCourse(String courseId);

    List<ResponseLessonDto> getTeacherLessons();

    LessonVideoResponse getVideo(String lessonId, String videoName) throws MalformedURLException;

    Lesson findLessonById(String lessonId);

    void deleteLesson(String lessonId);

    void deleteTeacherLessons();


}
