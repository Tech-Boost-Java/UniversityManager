package com.softserve.academy.service;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.repository.CourseRepository;
import com.softserve.academy.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling Teacher-related business logic.
 */
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Get all teachers.
     *
     * @return list of all teachers
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Get teacher by ID.
     *
     * @param id the teacher ID
     * @return the teacher if found, otherwise empty Optional
     */
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    /**
     * Save a teacher.
     *
     * @param teacher the teacher to save
     * @return the saved teacher
     */
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    /**
     * Delete a teacher by ID.
     *
     * @param id the teacher ID to delete
     */
    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + id));

        // Remove teacher from all assigned courses
        for (Course course : teacher.getCourses()) {
            course.setTeacher(null);
            courseRepository.save(course);
        }

        teacherRepository.deleteById(id);
    }

    /**
     * Find teacher by email.
     *
     * @param email the email to search for
     * @return the teacher if found, otherwise empty Optional
     */
    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    /**
     * Find teachers by name.
     *
     * @param name the name to search for
     * @return list of teachers with matching name
     */
    public List<Teacher> findByName(String name) {
        return teacherRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get courses taught by a teacher.
     *
     * @param teacherId the teacher ID
     * @return list of courses taught by the teacher
     */
    public List<Course> getTeacherCourses(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    /**
     * Count the number of courses taught by a teacher.
     *
     * @param teacherId the teacher ID
     * @return the number of courses taught by the teacher
     */
    public Long countCoursesByTeacher(Long teacherId) {
        return teacherRepository.countCoursesByTeacherId(teacherId);
    }

    /**
     * Assign a course to a teacher.
     *
     * @param teacherId the teacher ID
     * @param courseId the course ID
     * @return the updated teacher
     */
    @Transactional
    public Teacher assignCourseToTeacher(Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        teacher.addCourse(course);
        return teacherRepository.save(teacher);
    }

    /**
     * Remove a course from a teacher.
     *
     * @param teacherId the teacher ID
     * @param courseId the course ID
     * @return the updated teacher
     */
    @Transactional
    public Teacher removeCourseFromTeacher(Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        teacher.removeCourse(course);
        return teacherRepository.save(teacher);
    }
}