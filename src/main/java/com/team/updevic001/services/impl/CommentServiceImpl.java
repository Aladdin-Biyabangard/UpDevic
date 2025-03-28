package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CommentMapper;
import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.repositories.CommentRepository;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.dao.repositories.LessonRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CommentDto;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.services.interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CourseRepository courseRepository;
    private final CommentMapper commentMapper;
    private final LessonRepository lessonRepository;
    private final UserServiceImpl userServiceImpl;
    private final CommentRepository commentRepository;

    @Override
    public void addCommentToCourse(String userId, String courseId, CommentDto commentDto) {
        User user = userServiceImpl.findUserById(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such course."));
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .course(course)
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void addCommentToLesson(String userId, String lessonId, CommentDto commentDto) {
        User user = userServiceImpl.findUserById(userId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such lesson."));
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .lesson(lesson)
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(String commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("This comment has been deleted."));
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public List<ResponseCommentDto> getCourseComment(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such course."));
        List<Comment> comments = course.getComments();
        return !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
    }

    @Override
    public List<ResponseCommentDto> getLessonComment(String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such lesson."));
        List<Comment> comments = lesson.getComments();
        return !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
    }

    @Override
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
