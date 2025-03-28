package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.StudentCourse;
import com.team.updevic001.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, String> {
    @Query("SELECT uc.student FROM StudentCourse uc WHERE uc.course.id = :courseId")
    List<User> findUsersByCourse(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c JOIN StudentCourse sc ON sc.course = c WHERE sc.student = :student")
    List<Course> findCourseByStudent(@Param("student") Student student);

    Optional<StudentCourse> findByStudentAndCourse(Student student, Course course);

    boolean existsByCourseAndStudent(Course course, Student student);
}
