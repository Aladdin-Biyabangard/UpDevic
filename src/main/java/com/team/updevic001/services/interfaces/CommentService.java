package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.model.dtos.request.CommentDto;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;

import java.util.List;

public interface CommentService {

    void addCommentToCourse(String courseId, CommentDto commentDto);

    void addCommentToLesson(String lessonId, CommentDto commentDto);

    void updateComment(String commentId, CommentDto commentDto);


    List<ResponseCommentDto> getCourseComment(String courseId);

    List<ResponseCommentDto> getLessonComment(String lessonId);

    public Comment findCommentById(String commentId);


    void deleteComment(String commentId);
}
