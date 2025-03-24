package com.team.updevic001.services.impl;

import com.team.updevic001.config.mappers.CategoryMapper;
import com.team.updevic001.config.mappers.CommentMapper;
import com.team.updevic001.config.mappers.CourseMapper;
import com.team.updevic001.config.mappers.LessonMapper;
import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.CourseCategory;
import com.team.updevic001.dao.repositories.CourseCategoryRepository;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.enums.CourseCategoryType;
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
    private final CourseMapper courseMapper;
    private final CommentMapper commentMapper;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final LessonMapper lessonMapper;

    @Override
    public List<ResponseCourseDto> getCourse(String keyword) {
        List<Course> courses = findCourseBy(keyword);
        List<ResponseCourseDto> responseCourse = courses.stream().map(
                course -> {
                    int lessonCount = course.getLessons().size();
                    int studentCount = course.getStudentCourses().size();
                    int teacherCount = course.getTeacherCourses().size();

                    ResponseCourseDto responseCourseDto = courseMapper.courseDto(course);
                    responseCourseDto.setCommentDtoS(commentMapper.toDto(course.getComments()));
                    responseCourseDto.setLessonCount(lessonCount);
                    responseCourseDto.setStudentCount(studentCount);
                    responseCourseDto.setTeacherCount(teacherCount);
                    return responseCourseDto;
                }).toList();
        return responseCourse;
    }

    @Override
    public List<ResponseCourseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return !courses.isEmpty() ? courseMapper.courseDto(courses) : List.of();
    }


    @Override
    public List<ResponseCategoryDto> getCategory(CourseCategoryType categoryType) {
        String category = categoryType.name();
        List<CourseCategory> courseCategories = courseCategoryRepository.searchCategoryByKeyword(category);
        return courseCategories.stream().map(categoryMapper::toDto).toList();
    }

    @Override
    public List<ResponseLessonDto> getCourseLessons(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("There are no lessons in this course."));
        return !course.getLessons().isEmpty() ? lessonMapper.toDto(course.getLessons()) : List.of();
    }

    @Override
    public List<ResponseCommentDto> getCourseComment(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("There are no lessons in this course."));
        List<Comment> comments = course.getComments();
        return !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
    }

    @Override
    public void deleteCourseComment(String courseId, String commentId) {

    }

    public List<Course> findCourseBy(String keyword) {
        return courseRepository.searchCoursesByKeyword(keyword);
    }
}
