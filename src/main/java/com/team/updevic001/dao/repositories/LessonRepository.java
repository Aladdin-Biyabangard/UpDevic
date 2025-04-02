package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, String> {

    List<Lesson> findLessonByCourseId(String courseId);

}
