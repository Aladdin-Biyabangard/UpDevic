package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;

    @Override
    public ResponseCourseDto getCourse(String courseId) {
        return null;
    }

    @Override
    public List<ResponseCourseDto> getCourses() {
        return List.of();
    }

    @Override
    public List<ResponseCourseLessonDto> getCourseLessons(String courseId) {
        return List.of();
    }

    @Override
    public List<ResponseCommentDto> getCourseComment(String courseId) {
        return List.of();
    }

    @Override
    public void deleteCourseComment(String courseId, String commentId) {

    }

    public Course findCourseById(String courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found these course ID:" + courseId));
    }
}
