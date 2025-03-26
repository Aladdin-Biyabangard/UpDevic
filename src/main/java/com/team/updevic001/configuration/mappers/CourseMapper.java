package com.team.updevic001.configuration.mappers;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final LessonMapper lessonMapper;
    private final ModelMapper modelMapper;
    private final CommentMapper commentMapper;


    public ResponseCourseLessonDto toDto(Course course) {
        return new ResponseCourseLessonDto(
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getCreatedAt(),
                lessonMapper.toDto(course.getLessons()),
                commentMapper.toDto(course.getComments())
        );
    }


    public ResponseCourseDto courseDto(Course course) {
        return new ResponseCourseDto(
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getCreatedAt(),
                lessonCount(course),
                studentCount(course),
                teacherCount(course),
                course.getStatus(),
                commentMapper.toDto(course.getComments())
        );
    }

    public ResponseCourseShortInfoDto courseShortInfoDto(Course course) {
        return modelMapper.map(course, ResponseCourseShortInfoDto.class);
    }

    public List<ResponseCourseLessonDto> toDto(List<Course> courses) {
        return courses.stream().map(this::toDto).toList();
    }

    public List<ResponseCourseDto> courseDto(List<Course> courses) {
        return courses.stream().map(this::courseDto).toList();
    }

    public List<ResponseCourseShortInfoDto> courseShortInfoDto(List<Course> courses) {
        return courses.stream().map(this::courseShortInfoDto).toList();
    }

    private int lessonCount(Course course) {
        return course.getLessons().size();
    }

    private int studentCount(Course course) {
        return course.getStudentCourses().size();
    }

    private int teacherCount(Course course) {
        return course.getTeacherCourses().size();
    }
}
