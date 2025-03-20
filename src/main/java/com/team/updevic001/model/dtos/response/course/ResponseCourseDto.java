package com.team.updevic001.model.dtos.response.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.enums.CourseCategory;
import com.team.updevic001.model.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCourseDto {

    private String title;

    private String description;

    private CourseLevel level;

    private LocalDateTime createdAt;

    private List<ResponseCommentDto> commentDtoS;

    private Long lessonCount;

    private Long studentCount;

    private Long teacherCount;

    @JsonBackReference
    private CourseCategory category;

}
