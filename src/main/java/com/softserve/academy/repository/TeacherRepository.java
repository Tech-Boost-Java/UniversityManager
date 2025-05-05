package com.softserve.academy.repository;

import com.softserve.academy.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Find teacher by email
     * @param email the email to search for
     * @return an Optional containing the teacher if found
     */

    Optional<Teacher> findByEmail(String email);

    /**
     * Find teacher by first name or last name
     * @param name the name to search for
     * @return a list of teachers with the given name
     */
    @Query("SELECT t FROM Teacher t WHERE LOWER(t.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Teacher> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT t FROM Teacher t JOIN t.courses c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :courseName, '%'))")
    List<Teacher> findByCourseName(String courseName);


    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.id = :teacherId")
    Long countCoursesByTeacherId(Long teacherId);
}
