package com.team.updevic001.services;

public interface CommentService {

    void addCommentToCourse(String userId, String courseId, String content);

    void addCommentToLesson(String userId, String lessonId, String content);

    void updateCourseComment(String userId, String courseId, String commentId, String content);
}
