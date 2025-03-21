package com.team.updevic001.config.mappers;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {

    private final LessonMapper lessonMapper;

    public CourseMapper(LessonMapper lessonMapper) {
        this.lessonMapper = lessonMapper;
    }

    public ResponseCourseLessonDto toDto(Course course) {
        return new ResponseCourseLessonDto(
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getCreatedAt(),
                lessonMapper.toDto(course.getLessons())
        );
    }

    public List<ResponseCourseLessonDto> toDto(List<Course> courses){
        return courses.stream().map(this::toDto).toList();
    }
}
