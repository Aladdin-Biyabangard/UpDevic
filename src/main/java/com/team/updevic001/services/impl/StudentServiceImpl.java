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
import com.team.updevic001.utility.AuthHelper;
import jakarta.transaction.Transactional;
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
    private final CourseServiceImpl courseServiceImpl;
    private final AuthHelper authHelper;

    @Override
    @Transactional
    public void enrollInCourse(String courseId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Attempting to enroll user with ID: {} in course with ID: {}", user.getId(), courseId);

        Student student = castToStudent(user);

        Course course = courseServiceImpl.findCourseById(courseId);

        if (isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is already enrolled in course with ID: {}", user.getId(), courseId);
            throw new IllegalStateException("Student is already enrolled in this course!");
        }

        enrollStudentInCourse(student, course);
        log.info("Student with ID: {} successfully enrolled in course with ID: {}", user.getId(), courseId);
    }

    @Override
    @Transactional
    public void unenrollUserFromCourse(String courseId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Attempting to unenroll user with ID: {} from course with ID: {}", user.getId(), courseId);
        Student student = castToStudent(user);

        Course course = courseServiceImpl.findCourseById(courseId);

        if (!isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is not enrolled in course with ID: {}", user.getId(), courseId);
            throw new IllegalStateException("Only students can unenroll from courses!");
        }

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course!"));

        studentCourseRepository.delete(studentCourse);
        log.info("Student with ID: {} successfully unenrolled from course with ID: {}", user.getId(), courseId);
    }

    @Override
    public ResponseCourseShortInfoDto getStudentCourse(String courseId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Fetching course for student with ID: {}", user.getId());

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
        User user = authHelper.getAuthenticatedUser();
        log.info("Fetching courses for student with ID: {}", user.getId());

        Student student = castToStudent(user);
        List<Course> courseByStudent = studentCourseRepository.findCourseByStudent(student);

        log.info("Found {} courses for student with ID: {}", courseByStudent.size(), user.getId());
        return courseByStudent.stream()
                .map(course -> modelMapper.map(course, ResponseCourseShortInfoDto.class))
                .toList();
    }


    @Override
    @Transactional
    public void deleteStudentCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to delete course enrollment for student with ID: {} in course with ID: {}", authenticatedUser.getUuid(), courseId);
        Student student = castToStudent(authenticatedUser);
        Course course = findCourseById(courseId);

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new ResourceNotFoundException("This user is not registered for any courses."));

        studentCourseRepository.delete(studentCourse);
        log.info("Successfully deleted course enrollment for student with ID: {} in course with ID: {}", authenticatedUser.getUuid(), courseId);
    }

    @Override
    public List<ResponseCourseLessonDto> getStudentLessons() {
        User user = authHelper.getAuthenticatedUser();
        log.info("Fetching lessons for student with ID: {}", user.getId());

        Student student = castToStudent(user);
        List<Course> courses = studentCourseRepository.findCourseByStudent(student);

        return courseMapper.toDto(courses);
    }


    private boolean isAlreadyEnrolledInCourse(Student student, Course course) {
        return studentCourseRepository.existsByCourseAndStudent(course, student);
    }


    // Helper methods


    private void enrollStudentInCourse(Student student, Course course) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(course);
        studentCourse.setStudent(student);
        studentCourse.setStatus(Status.ENROLLED);
        studentCourseRepository.save(studentCourse);
    }


    @Override
    public Student castToStudent(User user) {
        if (!(user instanceof Student)) {
            log.error("User with ID: {} is not a student!", user.getId());
            throw new IllegalStateException("User is not a student!");
        }
        return (Student) user;
    }
}


//    @Override
//    public void deleteStudentLessonComment(String userId, String lessonId, String commentId) {
//        log.info("Attempting to delete comment with ID: {} from lesson with ID: {} for student with ID: {}", commentId, lessonId, userId);
//
//        User user = adminServiceImpl.findUserById(userId);
//        Lesson lesson = lessonServiceImpl.findLessonById(lessonId);
//        Comment comment = commentServiceImpl.findCommentById(commentId);
//
//        Comment findComment = user.getComments().stream()
//                .filter(comm -> comm.getLesson().equals(lesson) && comm.getId().equals(comment.getId()))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this lesson"));
//
//        user.getComments().remove(findComment);
//        userRepository.save(user);
//        commentRepository.delete(comment);
//
//        log.info("Successfully deleted comment with ID: {} from lesson with ID: {}", commentId, lessonId);
    /*

    @Override
     public void deleteStudentCourseComment(String userId, String courseId, String commentId) {
         log.info("Attempting to delete comment with ID: {} from course with ID: {} for student with ID: {}", commentId, courseId, userId);

         User user = adminServiceImpl.findUserById(userId);
         Course course = courseServiceImpl.findCourseById(courseId);

         Comment comment = commentServiceImpl.findCommentById(commentId);
         Comment findComment = user.getComments().stream()
                 .filter(comm -> comm.getCourse().equals(course) && comm.getId().equals(comment.getId()))
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException("User has no comment for this course"));

         user.getComments().remove(findComment);
         userRepository.save(user);
         commentRepository.delete(comment);

         log.info("Successfully deleted comment with ID: {} from course with ID: {}", commentId, courseId);
    // Helper methods


    private Course findCourseById(String courseId) {
        log.debug("Looking for course with ID: {}", courseId);
        return courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!"));
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
