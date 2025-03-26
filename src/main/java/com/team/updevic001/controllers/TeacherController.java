package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.CourseDto;
import com.team.updevic001.model.dtos.request.LessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.lesson.ResponseLessonDto;
import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherWithCourses;
import com.team.updevic001.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherServiceImpl;

//    // Yeni müəllim əlavə etmək
//    @PostMapping(path = "create")
//    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto teacherDto) {
//        Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
//        teacherRepository.save(teacher);
//        return ResponseEntity.ok(teacher);
//    }

    // Müəllimə kurs təyin etmək
    @PostMapping(path = "/{teacherId}/course/assign")
    public ResponseEntity<ResponseTeacherWithCourses> assignCourseToTeacher(@PathVariable String teacherId,
                                                                            @RequestBody CourseDto courseDto) {
        ResponseTeacherWithCourses teacherCourse = teacherServiceImpl.createTeacherCourse(teacherId, courseDto);
        return ResponseEntity.ok(teacherCourse);
    }

    // Müəllimə dərs təyin etmək
    @PostMapping(path = "/{teacherId}/course/{courseId}/lesson/assign")
    public ResponseEntity<ResponseLessonDto> assignLessonToCourse(@PathVariable String teacherId,
                                                                  @PathVariable String courseId,
                                                                  @RequestBody LessonDto lessonDto) {
        ResponseLessonDto responseLessonDto = teacherServiceImpl.assignLessonToCourse(teacherId, courseId, lessonDto);
        return ResponseEntity.ok(responseLessonDto);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Müəllimə aid kurs məlumatlarını yeniləmək
    @PutMapping(path = "/{teacherId}/course/{courseId}/update")
    public ResponseEntity<Void> updateTeacherCourse(@PathVariable String teacherId,
                                                    @PathVariable String courseId,
                                                    @RequestBody CourseDto courseDto) {
        teacherServiceImpl.updateTeacherCourseInfo(teacherId, courseId, courseDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Müəllimə aid dərs məlumatlarını yeniləmək
    @PutMapping(path = "/{teacherId}/lesson/{lessonId}/update")
    public ResponseEntity<Void> updateTeacherLesson(@PathVariable String teacherId,
                                                    @PathVariable String lessonId,
                                                    @RequestBody LessonDto lessonDto) {
        teacherServiceImpl.updateTeacherLessonInfo(teacherId, lessonId, lessonDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Müəllimə aid kurs məlumatlarını əldə etmək
    @GetMapping(path = "/{teacherId}/course/{courseId}/info")
    public ResponseEntity<ResponseCourseShortInfoDto> getTeacherCourse(@PathVariable String teacherId,
                                                                       @PathVariable String courseId) {
        ResponseCourseShortInfoDto teacherCourse = teacherServiceImpl.getTeacherCourse(teacherId, courseId);
        return ResponseEntity.ok(teacherCourse);
    }

    // Müəllimi və onun kurslarını əldə etmək
    @GetMapping(path = "/{teacherId}/courses")
    public ResponseEntity<List<ResponseCourseShortInfoDto>> getTeacherAndCourses(@PathVariable String teacherId) {
        List<ResponseCourseShortInfoDto> teacherAndCourses = teacherServiceImpl.getTeacherAndRelatedCourses(teacherId);
        return ResponseEntity.ok(teacherAndCourses);
    }

    // Müəllimin dərs məlumatlarını əldə etmək
    @GetMapping(path = "/{teacherId}/lesson/{lessonId}")
    public ResponseEntity<ResponseLessonDto> getTeacherLesson(@PathVariable String teacherId,
                                                              @PathVariable String lessonId) {
        ResponseLessonDto teacherLesson = teacherServiceImpl.getTeacherLesson(teacherId, lessonId);
        return ResponseEntity.ok(teacherLesson);
    }

    // Müəllimə aid kursa olan dərsləri əldə etmək
    @GetMapping(path = "/{teacherId}/course/{courseId}/lessons")
    public ResponseEntity<List<ResponseLessonDto>> getLessonsByCourse(@PathVariable String teacherId,
                                                                      @PathVariable String courseId) {
        List<ResponseLessonDto> teacherLessonsByCourse = teacherServiceImpl.getTeacherLessonsByCourse(teacherId, courseId);
        return ResponseEntity.ok(teacherLessonsByCourse);
    }

    // Müəllimin bütün dərslərini əldə etmək
    @GetMapping(path = "/{teacherId}/lessons")
    public ResponseEntity<List<ResponseLessonDto>> getTeacherLessons(@PathVariable String teacherId) {
        List<ResponseLessonDto> teacherLessons = teacherServiceImpl.getTeacherLessons(teacherId);
        return ResponseEntity.ok(teacherLessons);
    }

    // Müəllimin kursunu silmək
    @DeleteMapping(path = "/{teacherId}/course/{courseId}/delete")
    public ResponseEntity<Void> deleteTeacherCourse(@PathVariable String teacherId,
                                                    @PathVariable String courseId) {
        teacherServiceImpl.deleteTeacherCourse(teacherId, courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Müəllimin dərsini silmək
    @DeleteMapping(path = "/{teacherId}/lesson/{lessonId}/delete")
    public ResponseEntity<Void> deleteTeacherLesson(@PathVariable String teacherId,
                                                    @PathVariable String lessonId) {
        teacherServiceImpl.deleteTeacherLesson(teacherId, lessonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Müəllimin bütün kurslarını silmək
    @DeleteMapping(path = "/{teacherId}/courses/delete")
    public ResponseEntity<Void> deleteTeacherCourses(@PathVariable String teacherId) {
        teacherServiceImpl.deleteTeacherCourses(teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Müəllimin bütün dərslərini silmək
    @DeleteMapping(path = "/{teacherId}/lessons/delete")
    public ResponseEntity<Void> deleteTeacherLessons(@PathVariable String teacherId) {
        teacherServiceImpl.deleteTeacherLessons(teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Müəllimi silmək
    @DeleteMapping(path = "/delete/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String teacherId) {
        teacherServiceImpl.deleteTeacher(teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Bütün müəllimləri silmək
    @DeleteMapping(path = "/delete/all")
    public ResponseEntity<Void> deleteAllTeachers() {
        teacherServiceImpl.deleteAllTeachers();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
