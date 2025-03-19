package com.team.updevic001.model.dtos.request;

import com.team.updevic001.dao.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private String title;

    private String description;

    private String videoUrl;

    private Course course;

}
