package com.team.updevic001.model.dtos.response;

import com.team.updevic001.dao.entities.Certificate;
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

    private List<ResponseLessonDto> lessons;

    private List<ResponseCourseCommentDto> commentDtoS;

    private List<ResponseStudentDto> studentDtos;

    private List<ResponseTeacherDto> teacherDtos;

    private Certificate certificate;


}
