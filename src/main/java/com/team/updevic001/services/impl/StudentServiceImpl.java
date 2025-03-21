package com.team.updevic001.services.impl;

import com.team.updevic001.config.mappers.CourseMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.CommentRepository;
import com.team.updevic001.dao.repositories.LessonRepository;
import com.team.updevic001.dao.repositories.StudentCourseRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final CourseServiceImpl courseServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final StudentCourseRepository studentCourseRepository;
    private final ModelMapper modelMapper;
    private final CourseMapper courseMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Override
    public void enrollInCourse(String courseId, String userId) {
        User user = userServiceImpl.findUserById(userId);
        Student student = castToStudent(user);

        Course course = courseServiceImpl.findCourseById(courseId);

        if (isAlreadyEnrolledInCourse(student, course)) {
            throw new IllegalStateException("Student is already enrolled in this course!");
        }

        enrollStudentInCourse(student, course);
    }

    @Override
    public void unenrollUserFromCourse(String userId, String courseId) {
        User user = userServiceImpl.findUserById(userId);
        Student student = castToStudent(user);

        Course course = courseServiceImpl.findCourseById(courseId);

        if (isAlreadyEnrolledInCourse(student, course)) {
            throw new IllegalStateException("Only students can unenroll from courses!");
        }

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course!"));
        studentCourseRepository.delete(studentCourse);
    }

    @Override
    public String getCourseProgress(String userId, String courseId) {
        return "";
    }

    @Override
    public List<ResponseCourseShortInfoDto> getStudentCourse(String userId) {
        User user = userServiceImpl.findUserById(userId);
        Student student = castToStudent(user);
        List<Course> courseByStudent = studentCourseRepository.findCourseByStudent(student);
        return courseByStudent.stream().map(course -> modelMapper.map(course, ResponseCourseShortInfoDto.class)).toList();
    }

    @Override
    public List<ResponseCourseLessonDto> getStudentLesson(String userId) {
        User user = userServiceImpl.findUserById(userId);
        Student student = castToStudent(user);
        List<Course> courses = studentCourseRepository.findCourseByStudent(student);
        return courseMapper.toDto(courses);
    }

    @Override
    public void deleteStudentCourse(String userId, String courseId) {
        User user = userServiceImpl.findUserById(userId);
        Student student = castToStudent(user);
        Course course = courseServiceImpl.findCourseById(courseId);
        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new ResourceNotFoundException("This user is not registered for any courses."));

        studentCourseRepository.delete(studentCourse);
    }


    @Override
    public void deleteStudentCourseComment(String userId, String courseId, String commentId) {
        User user = userServiceImpl.findUserById(userId);

        Course courseById = courseServiceImpl.findCourseById(courseId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));

        List<Comment> comments = user.getComments();

        Comment findComment = comments.stream()
                .filter(comm -> comm.getCourse().equals(courseById) && comm.getUuid().equals(comment.getUuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this course"));

        user.getComments().remove(findComment);

        userRepository.save(user);

        commentRepository.delete(comment);
    }


    @Override
    public void deleteStudentLessonComment(String userId, String lessonId, String commentId) {
        User user = userServiceImpl.findUserById(userId);

        Lesson lesson = lessonRepository
                .findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson with ID " + lessonId + " not found!"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));
        List<Comment> comments = user.getComments();

        Comment findComment = comments.stream()
                .filter(comm -> comm.getLesson().equals(lesson) && comm.getUuid().equals(comment.getUuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User has no comment for this course"));

        user.getComments().remove(findComment);
        userRepository.save(user);

        commentRepository.delete(comment);

    }


    private Student castToStudent(User user) {
        if (!(user instanceof Student)) {
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
