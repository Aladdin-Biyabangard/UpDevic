package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LessonService {


    ResponseLessonShortInfoDto assignLessonToCourse(String courseId, LessonDto lessonDto, MultipartFile file) throws Exception;

    void updateTeacherLessonInfo(String lessonId, LessonDto lessonDto);

    Lesson findLessonById(String lessonId);

    ResponseLessonDto getLessonById(String lessonId);

    List<Lesson> getLessonsByCourse(String courseId);

    List<Lesson> getLessons();

    List<Lesson> getLessonComment(String lessonId);

}
