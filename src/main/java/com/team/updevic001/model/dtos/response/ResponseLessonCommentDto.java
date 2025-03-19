package com.team.updevic001.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonCommentDto {
    private String title;

    private String description;

    private String videoUrl;

    private String userName;
}
