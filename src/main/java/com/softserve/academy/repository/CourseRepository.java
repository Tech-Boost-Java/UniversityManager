package com.softserve.academy.repository;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Course entity operations.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Find a course by name.
     * 
     * @param name the name to search for
     * @return an Optional containing the course if found
     */
    Optional<Course> findByName(String name);
    
    /**
     * Find courses by teacher.
     * 
     * @param teacher the teacher to search for
     * @return a list of courses taught by the given teacher
     */
    List<Course> findByTeacher(Teacher teacher);
    
    /**
     * Find courses by teacher ID.
     * 
     * @param teacherId the teacher ID to search for
     * @return a list of courses taught by the teacher with the given ID
     */
    List<Course> findByTeacherId(Long teacherId);
    
    /**
     * Find courses containing the given text in their name or description.
     * 
     * @param searchText the text to search for
     * @return a list of courses matching the search criteria
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Course> searchCourses(String searchText);
}