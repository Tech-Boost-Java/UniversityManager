package com.softserve.academy.service;

import com.softserve.academy.model.Role;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for handling User-related business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public UserService(UserRepository userRepository, StudentService studentService, TeacherService teacherService) {
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    /**
     * Authenticate a user.
     *
     * @param username the username
     * @param password the password
     * @return the authenticated user if credentials are valid, otherwise empty Optional
     */
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }

        return Optional.empty();
    }

    /**
     * Register a new user with default role (ADMIN).
     *
     * @param user the user to register
     * @return the registered user
     * @throws IllegalArgumentException if username or email already exists
     */
    public User registerUser(User user) {
        return registerUser(user, Role.ADMIN);
    }

    /**
     * Register a new user with specified role.
     *
     * @param user the user to register
     * @param role the role to assign to the user
     * @return the registered user
     * @throws IllegalArgumentException if username or email already exists
     */
    @Transactional
    public User registerUser(User user, Role role) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Set the role
        user.setRole(role);

        // Save the user
        User savedUser = userRepository.save(user);

        // Create corresponding Student or Teacher record based on role
        if (role == Role.STUDENT) {
            // Check if student with this email already exists
            if (studentService.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Student with this email already exists");
            }

            // Create and save a new Student
            Student student = new Student();
            // Use the provided lastName if available, otherwise try to extract from username
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                student.setFirstName(user.getUsername());
                student.setLastName(user.getLastName());
            } else {
                // Fallback to old behavior: Split username into first and last name (assuming format: firstname.lastname)
                String[] nameParts = user.getUsername().split("\\.");
                if (nameParts.length > 1) {
                    student.setFirstName(nameParts[0]);
                    student.setLastName(nameParts[1]);
                } else {
                    // If username doesn't contain a dot, use username as first name and "Student" as last name
                    student.setFirstName(user.getUsername());
                    student.setLastName("Student");
                }
            }
            student.setEmail(user.getEmail());
            studentService.saveStudent(student);
        } else if (role == Role.TEACHER) {
            // Check if teacher with this email already exists
            if (teacherService.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Teacher with this email already exists");
            }

            // Create and save a new Teacher
            Teacher teacher = new Teacher();
            // Use the provided lastName if available, otherwise try to extract from username
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                teacher.setFirstName(user.getUsername());
                teacher.setLastName(user.getLastName());
            } else {
                // Fallback to old behavior: Split username into first and last name (assuming format: firstname.lastname)
                String[] nameParts = user.getUsername().split("\\.");
                if (nameParts.length > 1) {
                    teacher.setFirstName(nameParts[0]);
                    teacher.setLastName(nameParts[1]);
                } else {
                    // If username doesn't contain a dot, use username as first name and "Teacher" as last name
                    teacher.setFirstName(user.getUsername());
                    teacher.setLastName("Teacher");
                }
            }
            teacher.setEmail(user.getEmail());
            teacherService.saveTeacher(teacher);
        }

        return savedUser;
    }

    /**
     * Find a user by username.
     *
     * @param username the username to search for
     * @return the user if found, otherwise empty Optional
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find a user by email.
     *
     * @param email the email to search for
     * @return the user if found, otherwise empty Optional
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Check if a username already exists.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if an email already exists.
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
