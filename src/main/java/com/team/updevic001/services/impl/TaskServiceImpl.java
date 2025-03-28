package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.*;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.AnswerDto;
import com.team.updevic001.model.dtos.request.TaskDto;
import com.team.updevic001.model.dtos.response.task.ResponseTaskDto;
import com.team.updevic001.services.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;
    private final TestResultRepository testResultRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentTaskRepository studentTaskRepository;

    private final StudentCourseRepository studentCourseRepository;

    @Override
    public void createTask(String courseId, TaskDto taskDto) {
        log.info("Creating task for course: {}", courseId);
        Course course = findCourseById(courseId);

        Task task = modelMapper.map(taskDto, Task.class);

        List<String> options = formatedOptions(taskDto.getOptions());

        task.setOptions(options);
        task.setCorrectAnswer(taskDto.getCorrectAnswer());
        course.getTasks().add(task);
        task.setCourse(course);

        taskRepository.save(task);
        courseRepository.save(course);
        log.info("Task created and saved successfully for course: {}", courseId);
    }

    @Override
    public void checkAnswer(String studentId, String courseId, String taskId, AnswerDto answerDto) {
        log.info("Checking answer for student: {} in course: {} and task: {}", studentId, courseId, taskId);

        //TODO bu hissede holder den gelen useri yəni studenti gotureciyik
        Student student = findStudentById(studentId);

        Course course = findCourseById(courseId);
        // Bunu sonradan sileceiyik onsuz qeydiyyat etmeyibse testlere baxa bilmir
        ensureStudentIsEnrolled(student, course);

        Task task = findTaskById(taskId);

        checkCompletionTask();

        TestResult result = checkTestResult(student, course);

        validateAnswerAndUpdateScore(student, result, task, answerDto, course);
        log.info("Answer checked and score updated for student: {}", studentId);
    }

    @Override
    public List<ResponseTaskDto> getTasks(String courseId) {
        log.info("Fetching tasks for course: {}", courseId);
        List<Task> tasks = taskRepository.findTaskByCourseId(courseId);
        return tasks.stream().map(task -> modelMapper.map(task, ResponseTaskDto.class)).toList();
    }


    private List<String> formatedOptions(List<String> options) {
        log.debug("Formatting options: {}", options);
        final char[] optionChar = {'A'};
        return options.stream()
                .map(option -> optionChar[0]++ + ") " + option)
                .toList();
    }

    private void checkCompletionTask() {
        log.debug("Checking if task has already been completed...");
        if (studentTaskRepository.findStudentTaskByCompleted(true)) {
            log.error("This question has already been answered.");
            throw new IllegalArgumentException("This question has already been answered.");
        }
    }

    private TestResult checkTestResult(Student student, Course course) {
        log.debug("Fetching test result for student: {} and course: {}", student.getId(), course.getId());
        return testResultRepository
                .findTestResultByStudentAndCourse(student, course).orElseGet(
                        () -> {
                            TestResult testResult = new TestResult();
                            testResult.setScore(0);
                            testResult.setCourse(course);
                            testResult.setStudent(student);
                            return testResult;
                        });
    }

    private void validateAnswerAndUpdateScore(Student student, TestResult result, Task task, AnswerDto answerDto, Course course) {
        log.debug("Validating answer for student: {} and task: {}", student.getId(), task.getId());
        String correctAnswer = task.getCorrectAnswer();

        double percent = calculateScore(course);

        if (correctAnswer.contains(answerDto.getAnswer())) {
            result.setScore(result.getScore() + percent);
            testResultRepository.save(result);
            studentTaskRepository.save(new StudentTask(null, student, task, true));

            log.info("Answer correct. Score updated for student: {}", student.getId());
        } else {
            log.error("Incorrect answer provided by student: {}", student.getId());
            throw new IllegalArgumentException("Incorrect answer!");
        }
    }

    private double calculateScore(Course course) {
        log.debug("Calculating score for course: {}", course.getId());
        int taskCount = course.getTasks().size();
        return (double) 100 / taskCount;
    }

    private Student findStudentById(String studentId) {
        log.debug("Fetching student with ID: {}", studentId);
        return studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", studentId);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
    }

    private Course findCourseById(String courseId) {
        log.debug("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found with ID: {}", courseId);
                    return new ResourceNotFoundException("COURSE_NOT_FOUND");
                });
    }

    private Task findTaskById(String taskId) {
        log.debug("Fetching task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", taskId);
                    return new ResourceNotFoundException("TASK_NOT_FOUND");
                });
    }

    private void ensureStudentIsEnrolled(Student student, Course course) {
        log.debug("Checking if student is enrolled in course: {} for student: {}", course.getId(), student.getId());
        studentCourseRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> {
                    log.error("Student {} is not enrolled in course {}", student.getId(), course.getId());
                    return new IllegalArgumentException("This student is not enrolled in this course.");
                });
    }
}