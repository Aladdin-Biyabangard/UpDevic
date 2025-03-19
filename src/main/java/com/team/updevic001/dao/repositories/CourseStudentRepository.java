package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, UUID> {
}
