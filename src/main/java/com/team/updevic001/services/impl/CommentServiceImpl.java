package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CommentMapper;
import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.repositories.CommentRepository;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.dao.repositories.LessonRepository;
import com.team.updevic001.exceptions.ForbiddenException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CommentDto;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.services.CommentService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CourseRepository courseRepository;
    private final CommentMapper commentMapper;
    private final LessonRepository lessonRepository;
    private final CommentRepository commentRepository;
    private final AuthHelper authHelper;

    @Override
    public void addCommentToCourse(String courseId, CommentDto commentDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such course."));
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(authenticatedUser)
                .course(course)
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void addCommentToLesson(String lessonId, CommentDto commentDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such lesson."));
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(authenticatedUser)
                .lesson(lesson)
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(String commentId, CommentDto commentDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("COMMENT_NOT_FOUND"));
        if (!comment.getUser().getUuid().equals(authenticatedUser.getUuid())) {
            log.error("User wit ID {} not allowed to delete comment with ID {}: User is not author of comment", authenticatedUser.getUuid(), commentId);
            throw new ForbiddenException("NOT_ALLOWED_UPDATE_COMMENT");
        }
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
        User authenticatedUser = authHelper.getAuthenticatedUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("COMMENT_NOT_FOUND"));
        if (!comment.getUser().getUuid().equals(authenticatedUser.getUuid())
                || comment.getCourse().getTeacherCourses().stream()
                .map(tc -> tc.getTeacher().getUser().getUuid())
                .noneMatch(authenticatedUser.getUuid()::equals)) {
            log.error("User with ID {} not allowed to delete comment: User must be either admin of the course or author of course", authenticatedUser.getUuid());
            throw new ForbiddenException("NOT_ALLOWED_DELETE_COMMENT");
        }
        commentRepository.deleteById(commentId);
    }
}
