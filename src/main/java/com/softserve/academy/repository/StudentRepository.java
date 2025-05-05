package com.softserve.academy.repository;

import com.softserve.academy.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Student entity operations.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find a student by email.
     * 
     * @param email the email to search for
     * @return an Optional containing the student if found
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Find students by last name.
     * 
     * @param lastName the last name to search for
     * @return a list of students with the given last name
     */
    List<Student> findByLastName(String lastName);
    
    /**
     * Find students by first name and last name.
     * 
     * @param firstName the first name to search for
     * @param lastName the last name to search for
     * @return a list of students with the given first and last name
     */
    List<Student> findByFirstNameAndLastName(String firstName, String lastName);
}