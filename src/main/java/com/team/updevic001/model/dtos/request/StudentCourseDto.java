package com.team.updevic001.model.dtos.request;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseDto {

    private Student student;

    private Course course;
}
