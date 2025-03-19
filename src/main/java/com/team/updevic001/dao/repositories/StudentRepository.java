package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
