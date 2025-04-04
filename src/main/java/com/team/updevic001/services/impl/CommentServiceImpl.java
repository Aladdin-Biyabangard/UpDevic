package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CommentMapper;
import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.repositories.CommentRepository;
import com.team.updevic001.exceptions.ResourceAlreadyExistException;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.CommentDto;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.services.interfaces.CommentService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final LessonServiceImpl lessonServiceImpl;
    private final CourseServiceImpl courseServiceImpl;
    private final AuthHelper authHelper;

    @Override
    public void addCommentToCourse(String courseId, CommentDto commentDto) {
        User user = authHelper.getAuthenticatedUser();
        Course course = courseServiceImpl.findCourseById(courseId);
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .course(course)
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void addCommentToLesson(String lessonId, CommentDto commentDto) {
        User user = authHelper.getAuthenticatedUser();
        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .lesson(lesson)
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(String commentId, CommentDto commentDto) {
        Comment comment = findCommentById(commentId);
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public List<ResponseCommentDto> getCourseComment(String courseId) {
        Course course = courseServiceImpl.findCourseById(courseId);
        List<Comment> comments = course.getComments();
        return !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
    }

    @Override
    public List<ResponseCommentDto> getLessonComment(String lessonId) {
        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);
        List<Comment> comments = lesson.getComments();
        return !comments.isEmpty() ? commentMapper.toDto(comments) : List.of();
    }

    @Override
    public void deleteComment(String commentId) {
        User user = authHelper.getAuthenticatedUser();
        Optional<Comment> comment = commentRepository.findCommentByIdAndUser(commentId, user);

        if (comment.isPresent()) {
            commentRepository.deleteById(commentId);
        }
        throw new ResourceAlreadyExistException("COMMENT_NOT_FOUND");
    }

    public Comment findCommentById(String commentId) {
        log.debug("Looking for comment with ID: {}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));
    }
}
