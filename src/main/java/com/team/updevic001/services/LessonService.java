package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;

import java.util.List;

public interface LessonService {

    ResponseLessonDto getLesson(String lessonId);

    List<ResponseLessonDto> getLessons();

    List<ResponseCommentDto> getLessonComment(String lessonId);

}
