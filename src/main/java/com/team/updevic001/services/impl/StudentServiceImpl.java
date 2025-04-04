package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.CommentRepository;
import com.team.updevic001.dao.repositories.CourseRepository;
import com.team.updevic001.dao.repositories.StudentCourseRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.interfaces.*;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentCourseRepository studentCourseRepository;
    private final ModelMapper modelMapper;
    private final CourseMapper courseMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final CommentService commentServiceImpl;
    private final AdminService adminServiceImpl;
    private final LessonService lessonServiceImpl;
    private final AuthHelper authHelper;

    @Override
    public void enrollInCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to enroll user with ID: {} in course with ID: {}", authenticatedUser.getId(), courseId);

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Student student = castToStudent(user);

        Course course = courseService.findCourseById(courseId);

        if (isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is already enrolled in course with ID: {}", authenticatedUser.getId(), courseId);
            throw new IllegalStateException("Student is already enrolled in this course!");
        }

        enrollStudentInCourse(student, course);
        log.info("Student with ID: {} successfully enrolled in course with ID: {}", authenticatedUser.getId(), courseId);
    }

    @Override
    public void unenrollUserFromCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to unenroll user with ID: {} from course with ID: {}", authenticatedUser.getId(), courseId);

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Student student = castToStudent(user);

        Course course = courseService.findCourseById(courseId);

        if (!isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is not enrolled in course with ID: {}", authenticatedUser.getId(), courseId);
            throw new IllegalStateException("Only students can unenroll from courses!");
        }

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course!"));

        studentCourseRepository.delete(studentCourse);
        log.info("Student with ID: {} successfully unenrolled from course with ID: {}", authenticatedUser.getId(), courseId);
    }

    @Override
    public ResponseCourseShortInfoDto getStudentCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Fetching course for student with ID: {}", authenticatedUser.getId());

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Student student = castToStudent(user);
        Course course = courseRepository
                .findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Not found course !"));
        studentCourseRepository
                .findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalArgumentException("This student does not have such a course"));

        return courseMapper.courseShortInfoDto(course);
    }

    @Override
    public List<ResponseCourseShortInfoDto> getStudentCourses() {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Fetching courses for student with ID: {}", authenticatedUser.getId());

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Student student = castToStudent(user);
        List<Course> courseByStudent = studentCourseRepository.findCourseByStudent(student);

        log.info("Found {} courses for student with ID: {}", courseByStudent.size(), authenticatedUser.getId());
        return courseByStudent.stream()
                .map(course -> modelMapper.map(course, ResponseCourseShortInfoDto.class))
                .toList();
    }

    @Override
    public List<ResponseCourseLessonDto> getStudentLessons() {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Fetching lessons for student with ID: {}", authenticatedUser.getId());

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Student student = castToStudent(user);
        List<Course> courses = studentCourseRepository.findCourseByStudent(student);

        return courseMapper.toDto(courses);
    }

    @Override
    public void deleteStudentCourseComment(String courseId, String commentId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to delete comment with ID: {} from course with ID: {} for student with ID: {}", commentId, courseId, authenticatedUser.getId());

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Course course = courseService.findCourseById(courseId);

        Comment comment = commentServiceImpl.findCommentById(commentId);
        Comment findComment = user.getComments().stream()
                .filter(comm -> comm.getCourse().equals(course) && comm.getId().equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this course"));

        user.getComments().remove(findComment);
        userRepository.save(user);
        commentRepository.delete(comment);

        log.info("Successfully deleted comment with ID: {} from course with ID: {}", commentId, courseId);
    }

    @Override
    public void deleteStudentLessonComment(String lessonId, String commentId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to delete comment with ID: {} from lesson with ID: {} for student with ID: {}", commentId, lessonId, authenticatedUser.getId());

        User user = adminServiceImpl.findUserById(authenticatedUser.getId());
        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);
        Comment comment = commentServiceImpl.findCommentById(commentId);

        Comment findComment = user.getComments().stream()
                .filter(comm -> comm.getLesson().equals(lesson) && comm.getId().equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this lesson"));

        user.getComments().remove(findComment);
        userRepository.save(user);
        commentRepository.delete(comment);

        log.info("Successfully deleted comment with ID: {} from lesson with ID: {}", commentId, lessonId);
    }

    // Helper methods

    @Override
    public Student castToStudent(User user) {
        if (!(user instanceof Student)) {
            log.error("User with ID: {} is not a student!", user.getId());
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
