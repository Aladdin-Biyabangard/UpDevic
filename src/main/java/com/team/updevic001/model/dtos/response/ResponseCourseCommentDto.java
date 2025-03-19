package com.team.updevic001.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCourseCommentDto {
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private ResponseCourseDto userName;


}
