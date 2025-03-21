package com.team.updevic001.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentForCourseDto {

    private String content;

    private String userId;

    private String courseId;

}

