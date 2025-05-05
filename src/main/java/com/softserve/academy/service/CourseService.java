package com.softserve.academy.service;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.repository.CourseRepository;
import com.softserve.academy.repository.StudentRepository;
import com.softserve.academy.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for handling Course-related business logic.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, 
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Get all courses.
     *
     * @return list of all courses
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Get course by ID.
     *
     * @param id the course ID
     * @return the course if found, otherwise empty Optional
     */
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    /**
     * Save a course.
     *
     * @param course the course to save
     * @return the saved course
     */
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Delete a course by ID.
     *
     * @param id the course ID to delete
     */
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Find course by name.
     *
     * @param name the name to search for
     * @return the course if found, otherwise empty Optional
     */
    public Optional<Course> findByName(String name) {
        return courseRepository.findByName(name);
    }

    /**
     * Assign a teacher to a course.
     *
     * @param courseId the course ID
     * @param teacherId the teacher ID
     * @return the updated course
     */
    @Transactional
    public Course assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));
        
        course.setTeacher(teacher);
        teacher.addCourse(course);
        
        return courseRepository.save(course);
    }

    /**
     * Get all students enrolled in a course.
     *
     * @param courseId the course ID
     * @return set of students enrolled in the course
     */
    public Set<Student> getEnrolledStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        
        return course.getStudents();
    }

    /**
     * Search courses by text in name or description.
     *
     * @param searchText the text to search for
     * @return list of courses matching the search criteria
     */
    public List<Course> searchCourses(String searchText) {
        return courseRepository.searchCourses(searchText);
    }

    /**
     * Get courses taught by a specific teacher.
     *
     * @param teacherId the teacher ID
     * @return list of courses taught by the teacher
     */
    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    /**
     * Add a student to a course.
     *
     * @param courseId the course ID
     * @param studentId the student ID
     * @return the updated course
     */
    @Transactional
    public Course addStudentToCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        course.addStudent(student);
        return courseRepository.save(course);
    }

    /**
     * Remove a student from a course.
     *
     * @param courseId the course ID
     * @param studentId the student ID
     * @return the updated course
     */
    @Transactional
    public Course removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        course.removeStudent(student);
        return courseRepository.save(course);
    }
}