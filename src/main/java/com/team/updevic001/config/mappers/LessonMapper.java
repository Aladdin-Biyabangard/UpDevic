package com.team.updevic001.config.mappers;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonMapper {

    private final ModelMapper modelMapper;
    private final CommentMapper commentMapper;

    public ResponseLessonDto toDto(Lesson lesson) {
        return new ResponseLessonDto(
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getVideoUrl(),
                commentMapper.toDto(lesson.getComments())
        );
    }

    public List<ResponseLessonDto> toDto(List<Lesson> lessons) {
        return lessons.stream().map(this::toDto).toList();
    }

}
