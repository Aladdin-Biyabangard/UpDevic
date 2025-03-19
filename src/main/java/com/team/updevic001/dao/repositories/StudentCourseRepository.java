package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, UUID> {
}
