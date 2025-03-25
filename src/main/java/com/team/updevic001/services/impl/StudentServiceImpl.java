package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserServiceImpl userServiceImpl;
    private final StudentCourseRepository studentCourseRepository;
    private final ModelMapper modelMapper;
    private final CourseMapper courseMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public void enrollInCourse(String courseId, String userId) {
        log.info("Attempting to enroll user with ID: {} in course with ID: {}", userId, courseId);

        User user = findUserById(userId);
        Student student = castToStudent(user);

        Course course = findCourseById(courseId);

        if (isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is already enrolled in course with ID: {}", userId, courseId);
            throw new IllegalStateException("Student is already enrolled in this course!");
        }

        enrollStudentInCourse(student, course);
        log.info("Student with ID: {} successfully enrolled in course with ID: {}", userId, courseId);
    }

    @Override
    public void unenrollUserFromCourse(String userId, String courseId) {
        log.info("Attempting to unenroll user with ID: {} from course with ID: {}", userId, courseId);

        User user = findUserById(userId);
        Student student = castToStudent(user);

        Course course = findCourseById(courseId);

        if (!isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is not enrolled in course with ID: {}", userId, courseId);
            throw new IllegalStateException("Only students can unenroll from courses!");
        }

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course!"));

        studentCourseRepository.delete(studentCourse);
        log.info("Student with ID: {} successfully unenrolled from course with ID: {}", userId, courseId);
    }

    @Override
    public ResponseCourseShortInfoDto getStudentCourse(String userId, String courseId) {
        log.info("Fetching course for student with ID: {}", userId);

        User user = findUserById(userId);
        Student student = castToStudent(user);
        Course course = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Not found course !"));
        studentCourseRepository
                .findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalArgumentException("This student does not have such a course"));

        return courseMapper.courseShortInfoDto(course);
    }

    @Override
    public List<ResponseCourseShortInfoDto> getStudentCourses(String userId) {
        log.info("Fetching courses for student with ID: {}", userId);

        User user = findUserById(userId);
        Student student = castToStudent(user);
        List<Course> courseByStudent = studentCourseRepository.findCourseByStudent(student);

        log.info("Found {} courses for student with ID: {}", courseByStudent.size(), userId);
        return courseByStudent.stream()
                .map(course -> modelMapper.map(course, ResponseCourseShortInfoDto.class))
                .toList();
    }


    @Override
    public List<ResponseCourseLessonDto> getStudentLessons(String userId) {
        log.info("Fetching lessons for student with ID: {}", userId);

        User user = findUserById(userId);
        Student student = castToStudent(user);
        List<Course> courses = studentCourseRepository.findCourseByStudent(student);

        return courseMapper.toDto(courses);
    }

    @Override
    public void deleteStudentCourse(String userId, String courseId) {
        log.info("Attempting to delete course enrollment for student with ID: {} in course with ID: {}", userId, courseId);

        User user = findUserById(userId);
        Student student = castToStudent(user);
        Course course = findCourseById(courseId);

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new ResourceNotFoundException("This user is not registered for any courses."));

        studentCourseRepository.delete(studentCourse);
        log.info("Successfully deleted course enrollment for student with ID: {} in course with ID: {}", userId, courseId);
    }

    @Override
    public void deleteStudentCourseComment(String userId, String courseId, String commentId) {
        log.info("Attempting to delete comment with ID: {} from course with ID: {} for student with ID: {}", commentId, courseId, userId);

        User user = findUserById(userId);
        Course courseById = findCourseById(courseId);

        Comment comment = findCommentById(commentId);
        Comment findComment = user.getComments().stream()
                .filter(comm -> comm.getCourse().equals(courseById) && comm.getUuid().equals(comment.getUuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this course"));

        user.getComments().remove(findComment);
        userRepository.save(user);
        commentRepository.delete(comment);

        log.info("Successfully deleted comment with ID: {} from course with ID: {}", commentId, courseId);
    }

    @Override
    public void deleteStudentLessonComment(String userId, String lessonId, String commentId) {
        log.info("Attempting to delete comment with ID: {} from lesson with ID: {} for student with ID: {}", commentId, lessonId, userId);

        User user = findUserById(userId);
        Lesson lesson = findLessonById(lessonId);
        Comment comment = findCommentById(commentId);

        Comment findComment = user.getComments().stream()
                .filter(comm -> comm.getLesson().equals(lesson) && comm.getUuid().equals(comment.getUuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this lesson"));

        user.getComments().remove(findComment);
        userRepository.save(user);
        commentRepository.delete(comment);

        log.info("Successfully deleted comment with ID: {} from lesson with ID: {}", commentId, lessonId);
    }

    // Helper methods
    private User findUserById(String userId) {
        log.debug("Looking for user with ID: {}", userId);
        return userServiceImpl.findUserById(userId);
    }

    private Course findCourseById(String courseId) {
        log.debug("Looking for course with ID: {}", courseId);
        return courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));
    }

    private Comment findCommentById(String commentId) {
        log.debug("Looking for comment with ID: {}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));
    }

    private Lesson findLessonById(String lessonId) {
        log.debug("Looking for lesson with ID: {}", lessonId);
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson with ID " + lessonId + " not found!"));
    }

    private Student castToStudent(User user) {
        if (!(user instanceof Student)) {
            log.error("User with ID: {} is not a student!", user.getUuid());
            throw new IllegalStateException("User is not a student!");
        }
        return (Student) user;
    }

    private boolean isAlreadyEnrolledInCourse(Student student, Course course) {
        return studentCourseRepository.existsByCourseAndStudent(course, student);
    }

    private void enrollStudentInCourse(Student student, Course course) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(course);
        studentCourse.setStudent(student);
        studentCourse.setStatus(Status.ENROLLED);
        studentCourseRepository.save(studentCourse);
    }
}
