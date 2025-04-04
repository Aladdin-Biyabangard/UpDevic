package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestResultRepository extends JpaRepository<TestResult, String> {

    Optional<TestResult> findTestResultByStudentAndCourse(Student student, Course course);
}
