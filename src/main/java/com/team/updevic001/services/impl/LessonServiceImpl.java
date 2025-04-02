package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.dao.repositories.LessonRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.services.interfaces.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    @Override
    public Lesson findLessonById(String lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found these Id"));
    }

    @Override
    public List<Lesson> getLessonsByCourse(String courseId) {
        return lessonRepository.findLessonByCourseId(courseId);
    }

    @Override
    public List<Lesson> getLessons() {
        return List.of();
    }

    @Override
    public List<Lesson> getLessonComment(String lessonId) {
        return List.of();
    }
}
