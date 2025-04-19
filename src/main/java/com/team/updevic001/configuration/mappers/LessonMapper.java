package com.team.updevic001.configuration.mappers;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonMapper {

    private final CommentMapper commentMapper;

    public ResponseLessonDto toDto(Lesson lesson) {
        return new ResponseLessonDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getVideoUrl(),
                lesson.getDuration(),
                commentMapper.toDto(lesson.getComments())
        );
    }

    public ResponseLessonShortInfoDto toShortLesson(Lesson lesson) {
        return new ResponseLessonShortInfoDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription()
        );
    }

    public List<ResponseLessonDto> toDto(List<Lesson> lessons) {
        return lessons.stream().map(this::toDto).toList();
    }

    public List<ResponseLessonShortInfoDto> toShortLesson(List<Lesson> lessons) {
        return lessons.stream().map(this::toShortLesson).toList();
    }

}
