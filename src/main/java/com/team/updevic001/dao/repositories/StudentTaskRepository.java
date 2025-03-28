package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.StudentTask;
import com.team.updevic001.dao.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {
    Optional<StudentTask> findStudentTaskByStudentAndTask(Student student, Task task);

    boolean findStudentTaskByCompleted(Boolean completed);
}
