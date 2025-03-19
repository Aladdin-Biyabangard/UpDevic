package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
}
