package com.team.updevic001.model.dtos.response.lesson;

import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonDto {

    private String lessonId;

    private String title;

    private String description;

    private String videoUrl;

    private List<ResponseCommentDto> comments;

}
