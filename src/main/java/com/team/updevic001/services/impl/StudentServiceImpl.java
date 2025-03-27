package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.course.ResponseCourseLessonDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.StudentService;
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
    private final CourseRepository courseRepository;
    private final AuthHelper authHelper;

    @Override
    @Transactional
    public void enrollInCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to enroll user with ID: {} in course with ID: {}", authenticatedUser.getUuid(), courseId);
        Student student = castToStudent(authenticatedUser);

        Course course = findCourseById(courseId);

        if (isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is already enrolled in course with ID: {}", authenticatedUser.getUuid(), courseId);
            throw new IllegalStateException("Student is already enrolled in this course!");
        }

        enrollStudentInCourse(student, course);
        log.info("Student with ID: {} successfully enrolled in course with ID: {}", authenticatedUser.getUuid(), courseId);
    }

    @Override
    @Transactional
    public void unenrollUserFromCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Attempting to unenroll user with ID: {} from course with ID: {}", authenticatedUser.getUuid(), courseId);
        Student student = castToStudent(authenticatedUser);

        Course course = findCourseById(courseId);

        if (!isAlreadyEnrolledInCourse(student, course)) {
            log.error("Student with ID: {} is not enrolled in course with ID: {}", authenticatedUser.getUuid(), courseId);
            throw new IllegalStateException("Only students can unenroll from courses!");
        }

        StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course!"));

        studentCourseRepository.delete(studentCourse);
        log.info("Student with ID: {} successfully unenrolled from course with ID: {}", authenticatedUser.getUuid(), courseId);
    }

    @Override
    public ResponseCourseShortInfoDto getStudentCourse(String courseId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Fetching course for student with ID: {}", authenticatedUser.getUuid());
        Student student = castToStudent(authenticatedUser);
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
        log.info("Fetching courses for student with ID: {}", authenticatedUser.getUuid());
        Student student = castToStudent(authenticatedUser);
        List<Course> courseByStudent = studentCourseRepository.findCourseByStudent(student);

        log.info("Found {} courses for student with ID: {}", courseByStudent.size(), authenticatedUser.getUuid());
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
        User authenticatedUser = authHelper.getAuthenticatedUser();

        log.info("Fetching lessons for student with ID: {}", authenticatedUser.getUuid());
        Student student = castToStudent(authenticatedUser);
        List<Course> courses = studentCourseRepository.findCourseByStudent(student);

        return courseMapper.toDto(courses);
    }
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
