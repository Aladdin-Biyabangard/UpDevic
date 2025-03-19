package com.team.updevic001.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonDto {

    private String title;

    private String description;

    private String videoUrl;

    private List<ResponseLessonCommentDto> comments;
}
