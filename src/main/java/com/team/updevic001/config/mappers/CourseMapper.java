package com.team.updevic001.config.mappers;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final LessonMapper lessonMapper;
    private final ModelMapper modelMapper;


    public ResponseCourseLessonDto toDto(Course course) {
        return new ResponseCourseLessonDto(
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getCreatedAt(),
                lessonMapper.toDto(course.getLessons())
        );
    }

    public List<ResponseCourseLessonDto> toDto(List<Course> courses) {
        return courses.stream().map(this::toDto).toList();
    }

    public ResponseCourseDto courseDto(Course course) {
        return modelMapper.map(course, ResponseCourseDto.class);
    }

    public List<ResponseCourseDto> courseDto(List<Course> courses) {
        return courses.stream().map(this::courseDto).toList();
    }
}
