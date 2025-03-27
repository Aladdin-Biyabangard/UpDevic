package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.CommentDto;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentServiceImpl;

    @PostMapping(path = "/{courseId}/course")
    public ResponseEntity<String> addCommentToCourse(@PathVariable String courseId,
                                                     @RequestBody CommentDto comment) {
        URI location = URI.create("/api/comment/" + courseId + "/course");
        commentServiceImpl.addCommentToCourse(courseId, comment);
        return ResponseEntity.created(location).body("Comment added successfully.");
    }

    @PostMapping(path = "/{lessonId}/lesson")
    public ResponseEntity<String> addCommentToLesson(@PathVariable String lessonId,
                                                     @RequestBody CommentDto comment) {
        commentServiceImpl.addCommentToLesson(lessonId, comment);
        return ResponseEntity.ok("Comment added successfully.");
    }

    @PutMapping(path = "{commentId}/update")
    public ResponseEntity<String> updateComment(@PathVariable String commentId,
                                                @RequestBody CommentDto commentDto) {
        commentServiceImpl.updateComment(commentId, commentDto);
        return ResponseEntity.ok("Comment updated successfully!");
    }

    @GetMapping(path = "{courseId}/course")
    public ResponseEntity<List<ResponseCommentDto>> getCourseComment(@PathVariable String courseId) {
        List<ResponseCommentDto> comments = commentServiceImpl.getCourseComment(courseId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping(path = "{lessonId}/lesson")
    public ResponseEntity<List<ResponseCommentDto>> getLessonComment(@PathVariable String lessonId) {
        List<ResponseCommentDto> comments = commentServiceImpl.getLessonComment(lessonId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping(path = "/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId) {
        commentServiceImpl.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully! ");
    }

}
