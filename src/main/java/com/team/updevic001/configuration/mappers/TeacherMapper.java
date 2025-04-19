package com.team.updevic001.configuration.mappers;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.TeacherCourse;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeacherMapper {

    private final CourseMapper courseMapper;

    public TeacherMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public ResponseTeacherWithCourses toDto(Teacher teacher) {
        return new ResponseTeacherWithCourses(
                teacher.getUser().getFirstName(),
                teacher.getUser().getLastName(),
                teacher.getSpeciality(),
                teacher.getExperienceYears(),
                courseMapper.courseDto(courses(teacher))
        );
    }

    public ResponseTeacherDto toTeacherDto(Teacher teacher) {
        return new ResponseTeacherDto(
                teacher.getUser().getFirstName(),
                teacher.getUser().getLastName(),
                teacher.getUser().getEmail(),
                teacher.getSpeciality(),
                teacher.getExperienceYears(),
                teacher.getUser().getUserProfile().getSocialLinks(),
                teacher.getHireDate()
        );
    }

    public List<ResponseTeacherDto> toTeacherDto(List<Teacher> teachers) {
        return teachers.stream().map(this::toTeacherDto).toList();
    }


    private List<Course> courses(Teacher teacher) {
        List<TeacherCourse> teacherCourses = teacher.getTeacherCourses();
        return teacherCourses.stream().map(TeacherCourse::getCourse).toList();
    }
}
