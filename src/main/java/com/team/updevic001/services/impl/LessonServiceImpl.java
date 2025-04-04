package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.LessonMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.LessonRepository;
import com.team.updevic001.dao.repositories.TeacherCourseRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonShortInfoDto;
import com.team.updevic001.services.interfaces.CourseService;
import com.team.updevic001.services.interfaces.LessonService;
import com.team.updevic001.services.interfaces.TeacherService;
import com.team.updevic001.services.interfaces.VideoService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final AuthHelper authHelper;
    private final TeacherService teacherService;
    private final ModelMapper modelMapper;
    private final CourseService courseService;
    private final VideoService videoServiceImpl;
    private final TeacherCourseRepository teacherCourseRepository;
    private final LessonMapper lessonMapper;

    @Value("${video.directory}")
    private String VIDEO_DIRECTORY;

    @Override
    public ResponseLessonShortInfoDto assignLessonToCourse(String courseId, LessonDto lessonDto, MultipartFile file) throws Exception {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Assigning lesson to course. Teacher ID: {}, Course ID: {}", authenticatedUser.getId(), courseId);
        Teacher teacher = teacherService.findTeacherByUserId(authenticatedUser.getId());

        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);

        Course course = courseService.findCourseById(courseId);

        courseService.findTeacherCourse(course, teacher);


        if (file != null && !file.isEmpty()) {
            String uploadedFileName = videoServiceImpl.uploadVideo(file);
            lesson.setVideoUrl(VIDEO_DIRECTORY + uploadedFileName);
        }

        lesson.setCourse(course);
        course.getLessons().add(lesson);
        lessonRepository.save(lesson);

        log.info("Lesson assigned successfully. Lesson ID: {}", lesson.getId());
        return modelMapper.map(lesson, ResponseLessonShortInfoDto.class);
    }

    @Override
    public void updateTeacherLessonInfo(String lessonId, LessonDto updatedLessonDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Updating teacher lesson info. Teacher ID: {}, Lesson ID: {}", authenticatedUser.getId(), lessonId);

        Teacher teacher = teacherService.findTeacherByUserId(authenticatedUser.getId());

        Lesson lesson = findLessonById(lessonId);

        List<TeacherCourse> teacherCourses = teacherCourseRepository.findTeacherCourseByTeacher(teacher);

        Optional<TeacherCourse> teacherCourse = teacherCourses.stream().filter(tc -> tc.getCourse().getLessons().contains(lesson)).findFirst();

        if (teacherCourse.isPresent()) {
            modelMapper.map(updatedLessonDto, lesson);
            lessonRepository.save(lesson);
        } else {
            throw new IllegalArgumentException("No such lesson found for this teacher.");
        }
    }


    @Override
    public Lesson findLessonById(String lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found these Id"));
    }

    @Override
    public ResponseLessonDto getLessonById(String lessonId) {
        Lesson lesson = findLessonById(lessonId);
        return lessonMapper.toDto(lesson);
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
