package com.team.updevic001.services.interfaces;

import com.team.updevic001.dao.entities.Lesson;

import java.util.List;

public interface LessonService {

    Lesson findLessonById(String lessonId);

    List<Lesson> getLessonsByCourse(String courseId);

    List<Lesson> getLessons();

    List<Lesson> getLessonComment(String lessonId);

}
