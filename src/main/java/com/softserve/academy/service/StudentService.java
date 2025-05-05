package com.softserve.academy.service;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.repository.CourseRepository;
import com.softserve.academy.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling Student-related business logic.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Get all students.
     *
     * @return list of all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Get student by ID.
     *
     * @param id the student ID
     * @return the student if found, otherwise empty Optional
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Save a student.
     *
     * @param student the student to save
     * @return the saved student
     */
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Delete a student by ID.
     *
     * @param id the student ID to delete
     */
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * Find student by email.
     *
     * @param email the email to search for
     * @return the student if found, otherwise empty Optional
     */
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    /**
     * Enroll a student in a course.
     *
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the updated student
     */
    @Transactional
    public Student enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        
        student.enrollInCourse(course);
        return studentRepository.save(student);
    }

    /**
     * Withdraw a student from a course.
     *
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the updated student
     */
    @Transactional
    public Student withdrawStudentFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        
        student.withdrawFromCourse(course);
        return studentRepository.save(student);
    }

    /**
     * Get all courses for a student.
     *
     * @param studentId the student ID
     * @return list of courses the student is enrolled in
     */
    public List<Course> getStudentCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        return student.getEnrolledCourses().stream().toList();
    }
}