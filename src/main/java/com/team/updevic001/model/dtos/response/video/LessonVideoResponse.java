package com.team.updevic001.model.dtos.response.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonVideoResponse {
    private String lessonTitle;
    private String lessonDescription;
    private Resource video;
}
