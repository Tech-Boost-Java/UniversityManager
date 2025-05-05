package com.softserve.academy;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.service.CourseService;
import com.softserve.academy.service.StudentService;
import com.softserve.academy.service.TeacherService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Student-Course-Teacher Management System.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class Application {


    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;

    public Application(StudentService studentService,
                       TeacherService teacherService,
                       CourseService courseService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initialize test data for the application.
     * Creates a test teacher, course, and student, and establishes relationships between them.
     */
    @PostConstruct
    public void initTestData() {
        // Create a test teacher
        Teacher teacher = Teacher.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@example.com")
                .build();

        teacher = teacherService.saveTeacher(teacher);

        // Create a test course
        Course course = Course.builder()
                .name("Java Programming")
                .description("Introduction to Java programming language")
                .teacher(teacher)
                .build();

        course = courseService.saveCourse(course);

        // Assign teacher to course
        courseService.assignTeacherToCourse(course.getId(), teacher.getId());

        // Create a test student
        Student student =
                Student.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane.doe@example.com")
                        .build();

        student = studentService.saveStudent(student);

        // Enroll student in course
        studentService.enrollStudentInCourse(student.getId(), course.getId());

        System.out.println("Test data initialized successfully!");
        System.out.println("Teacher: " + teacher.getName() + " (ID: " + teacher.getId() + ")");
        System.out.println("Course: " + course.getName() + " (ID: " + course.getId() + ")");
        System.out.println("Student: " + student.getFirstName() + " " + student.getLastName() + " (ID: " + student.getId() + ")");
    }
}
