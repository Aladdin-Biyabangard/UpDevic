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
import com.team.updevic001.services.interfaces.CommentService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CourseRepository courseRepository;
    private final CommentMapper commentMapper;
    private final LessonRepository lessonRepository;
    private final CommentRepository commentRepository;
    private final AuthHelper authHelper;
    private final AuthHelper authHelper;

    @Override
    @Transactional
    public void addCommentToCourse(String courseId, CommentDto commentDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of adding new comment to course with ID {} started by user with ID {}", courseId, authenticatedUser.getUuid());
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(Course.class));
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(authenticatedUser)
                .course(course)
                .build();
        commentRepository.save(comment);
        log.info("Comment successfully created to course with ID {}.", courseId);
    }

    @Override
    @Transactional
    public void addCommentToLesson(String lessonId, CommentDto commentDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of adding new comment to lesson with ID {} started by user with ID {}", lessonId, authenticatedUser.getUuid());
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(Lesson.class));
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(authenticatedUser)
                .lesson(lesson)
                .build();
        commentRepository.save(comment);
        log.info("Comment successfully created to lesson with ID {}.", lessonId);

    }

    @Override
    @Transactional
    public void updateComment(String commentId, CommentDto commentDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of updating comment with ID {} started by user with ID {}", commentId, authenticatedUser.getUuid());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(Comment.class));
        if (!comment.getUser().getUuid().equals(authenticatedUser.getUuid())) {
            log.error("User wit ID {} not allowed to delete comment with ID {}: User is not author of comment", authenticatedUser.getUuid(), commentId);
            throw new ForbiddenException("NOT_ALLOWED_UPDATE_COMMENT");
        }
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
        log.info("Comment with ID {} successfully updated", commentId);
    }

    @Override
    public List<ResponseCommentDto> getCourseComment(String courseId) {
        log.info("Operation of getting comments for course with ID {} started", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such course."));
        List<Comment> comments = course.getComments();
        List<ResponseCommentDto> commentDtos = !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
        log.info("Comments for course with ID {} are returned to user", courseId);
        return commentDtos;
    }

    @Override
    public List<ResponseCommentDto> getLessonComment(String lessonId) {
        log.info("Operation of getting comments for lesson with ID {} started", lessonId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no such lesson."));
        List<Comment> comments = lesson.getComments();
        List<ResponseCommentDto> commentDtos = !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
        log.info("Comments for lesson with ID {} are returned to user", lessonId);
        return commentDtos;
    }

    @Override
    @Transactional
    public void deleteComment(String commentId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of deleting comment with ID {} started by user with ID {}", commentId, authenticatedUser.getUuid());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(Comment.class));
        if (!comment.getUser().getUuid().equals(authenticatedUser.getUuid())
                || comment.getCourse().getTeacherCourses().stream()
                .map(tc -> tc.getTeacher().getUser().getUuid())
                .noneMatch(authenticatedUser.getUuid()::equals)) {
            log.error("User with ID {} not allowed to delete comment: User must be either admin of the course or author of course", authenticatedUser.getUuid());
            throw new ForbiddenException("NOT_ALLOWED_DELETE_COMMENT");
        }
        commentRepository.deleteById(commentId);
        log.info("Comment successfully deleted");
    }

    public Comment findCommentById(String commentId) {
        log.debug("Looking for comment with ID: {}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));
    }
}
