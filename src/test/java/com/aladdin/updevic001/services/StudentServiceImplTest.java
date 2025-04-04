package com.aladdin.updevic001.services;


import com.team.updevic001.configuration.mappers.CourseMapper;
import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.StudentCourse;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.services.impl.StudentServiceImpl;
import com.team.updevic001.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
 /*
    @InjectMocks
    private StudentServiceImpl studentService;
    @Mock
    private StudentCourseRepository studentCourseRepository;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private CourseRepository courseRepository;

    @Test
    void testEnrollInCourse_Success() {
        String userId = "user123";
        String courseId = "course123";
        Student student = new Student();
        student.setid(userId);
        student.setStudentNumber("ST123");
        Course course = new Course();
        ReflectionTestUtils.setField(course, "id", courseId);
        when(userServiceImpl.findUserById(userId)).thenReturn(student);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentCourseRepository.existsByCourseAndStudent(course, student)).thenReturn(false);
        studentService.enrollInCourse(courseId, userId);
        verify(studentCourseRepository, times(1)).save(any(StudentCourse.class));
    }

    @Test
    void testUnenrollerUserFromCourse_Success() {
        String userId = "user123";
        String courseId = "course123";
        Student student = new Student();
        student.setid(userId);
        student.setStudentNumber("St123");
        Course course = new Course();
        ReflectionTestUtils.setField(course, "id", courseId);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        when(userServiceImpl.findUserById(userId)).thenReturn(student);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentCourseRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.of(studentCourse));
        studentService.unenrollUserFromCourse(userId, courseId);
        verify(studentCourseRepository, times(1)).delete(studentCourse);
    }

    @Test
    void testGetStudentCourse_Success() {
        String userId = "user123";
        String courseId = "course123";

        Student student = new Student();
        student.setid(userId);
        student.setStudentNumber("ST123");
        Course course = new Course();
        ReflectionTestUtils.setField(course, "id", courseId);
        course.setTitle("Java Course");
        ResponseCourseShortInfoDto expectedDto = new ResponseCourseShortInfoDto();
        expectedDto.setTitle("Java Course");
        when(userServiceImpl.findUserById(userId)).thenReturn(student);
        when(courseRepository.findById(userId)).thenReturn(Optional.of(course));
        when(studentCourseRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.of(new StudentCourse()));
        ResponseCourseShortInfoDto result = studentService.getStudentCourse(userId, courseId);
        assertNotNull(result);
        assertEquals(expectedDto.getTitle(), result.getTitle());
    }

    @Test
    void testDeleteStudentCode_Success() {
        String userId = "user123";
        String courseId = "course123";

        Student student = new Student();
        student.setid(userId);
        student.setStudentNumber("ST123");
        Course course = new Course();
        ReflectionTestUtils.setField(course, "id", courseId);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        when(userServiceImpl.findUserById(userId)).thenReturn(student);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentCourseRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.of(studentCourse));
        studentService.deleteStudentCourse(userId, courseId);
        verify(studentCourseRepository, times(1)).delete(studentCourse);

    }
*/

}
