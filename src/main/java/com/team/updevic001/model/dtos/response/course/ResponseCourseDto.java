package com.team.updevic001.model.dtos.response.course;

import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import com.team.updevic001.model.enums.CourseCategoryType;
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

    private String courseId;

    private CourseCategoryType category;

    private String headTeacher;

    private List<String> teachers;

    private String title;

    private String description;

    private CourseLevel level;

    private LocalDateTime createdAt;

    private long lessonCount;

    private long studentCount;

    private long teacherCount;

    private List<ResponseCommentDto> commentDtoS;

}
