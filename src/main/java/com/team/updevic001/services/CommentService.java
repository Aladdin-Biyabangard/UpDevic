package com.team.updevic001.services;

import com.team.updevic001.model.dtos.request.CommentDto;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;

import java.util.List;

public interface CommentService {

    void addCommentToCourse(String userId, String courseId, CommentDto commentDto);

    void addCommentToLesson(String userId, String lessonId, CommentDto commentDto);

    void updateComment(String commentId, CommentDto commentDto);


    List<ResponseCommentDto> getCourseComment(String courseId);

    List<ResponseCommentDto> getLessonComment(String lessonId);


    void deleteComment(String commentId);
}
