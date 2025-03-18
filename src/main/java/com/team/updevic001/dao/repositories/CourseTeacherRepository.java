package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.CourseTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, UUID> {
}
