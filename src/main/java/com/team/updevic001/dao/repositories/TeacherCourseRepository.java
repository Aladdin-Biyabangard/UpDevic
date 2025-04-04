package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, String> {

    Optional<TeacherCourse> findByCourseAndTeacher(Course course, Teacher teacher);

    List<TeacherCourse> findTeacherCourseByTeacher(Teacher teacher);

  Optional<TeacherCourse> findByCourseIdAndTeacher(String courseId, Teacher authenticatedTeacher);

}
