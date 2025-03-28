package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findTaskByCourseId(String courseId);

}
