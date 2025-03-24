package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE teachers AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
