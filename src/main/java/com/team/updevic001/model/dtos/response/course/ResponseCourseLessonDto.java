package com.team.updevic001.model.dtos.response.course;

import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCourseLessonDto {

    private String title;

    private String description;

    private CourseLevel level;

    private LocalDateTime createdAt;

    private List<ResponseLessonDto> lessons;
}
