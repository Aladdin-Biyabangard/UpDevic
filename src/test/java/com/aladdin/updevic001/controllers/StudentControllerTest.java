package com.aladdin.updevic001.controllers;


import com.team.updevic001.controllers.StudentController;

import com.team.updevic001.services.interfaces.StudentService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentService studentService;
        private MockMvc mockMvc;

//    @InjectMocks
//    private StudentController studentController;
//    private ObjectMapper objectMapper;
/*
    @BeforeEach
    void setup() {
      this.mockMvc= MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testEnrollInCourse() throws Exception {
        String userId = "user123";
        String courseId = "course123";
        doNothing().when(studentService).enrollInCourse(courseId, userId);
        mockMvc.perform(post("/enroll")
                        .param("userId",userId)
                        .param("courseId", courseId))
                .andExpect(status().isOk())
                .andExpect(content().string("Student successfully enrolled in the course"));
        verify(studentService, times(1)).enrollInCourse(courseId, userId);
    }

    @Test
    void testUnenrollFromCourse() throws Exception {
        String userId = "user123";
        String courseId = "course123";
        doNothing().when(studentService).unenrollUserFromCourse(userId, courseId);
        mockMvc.perform(delete("/unenroll")
                        .param("userId", userId)
                        .param("courseId", courseId))
                .andExpect(status().isOk())
                .andExpect(content().string("Student successfully unenrolled from the course."));
        verify(studentService, times(1)).unenrollUserFromCourse(userId, courseId);
    }

    @Test
    void testGetStudentCourses() throws Exception {
        String userId = "user123";
        List<ResponseCourseShortInfoDto> courses = new ArrayList<>();
        ResponseCourseShortInfoDto course = new ResponseCourseShortInfoDto();
        course.setDescription("Course Description");
        course.setTitle("Course 1");
        courses.add(course);
        when(studentService.getStudentCourse(userId)).thenReturn(courses);


        mockMvc.perform(get("/courses")
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(studentService, times(1)).getStudentCourse(userId);
    }

    @Test
    void testDeleteStudentCourse() throws Exception {
        String userId = "user123";
        String courseId = "course123";
        doNothing().when(studentService).deleteStudentCourse(userId, courseId);
        mockMvc.perform(delete("/delete-course")
                        .param("userId", userId)
                        .param("courseId", courseId))
                .andExpect(status().isOk())
                .andExpect(content().string("Student's course enrollment successfully deleted."));
        verify(studentService, times(1)).deleteStudentCourse(userId, courseId);


    }
*/
}
