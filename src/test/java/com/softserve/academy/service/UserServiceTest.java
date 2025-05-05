package com.softserve.academy.service;

import com.softserve.academy.model.Role;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.model.User;
import com.softserve.academy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john.doe", "password123", "john.doe@example.com");
    }

    @Test
    void registerUser_withStudentRole_shouldCreateStudentRecord() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.registerUser(user, Role.STUDENT);

        // then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).save(user);
        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    void registerUser_withTeacherRole_shouldCreateTeacherRecord() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.registerUser(user, Role.TEACHER);

        // then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).save(user);
        verify(teacherService, times(1)).saveTeacher(any(Teacher.class));
    }

    @Test
    void registerUser_withAdminRole_shouldNotCreateStudentOrTeacherRecord() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.registerUser(user, Role.ADMIN);

        // then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).save(user);
        verify(studentService, never()).saveStudent(any(Student.class));
        verify(teacherService, never()).saveTeacher(any(Teacher.class));
    }

    @Test
    void registerUser_whenUsernameExists_shouldThrowException() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user, Role.STUDENT);
        });
        verify(userRepository, never()).save(any(User.class));
        verify(studentService, never()).saveStudent(any(Student.class));
    }

    @Test
    void registerUser_whenEmailExists_shouldThrowException() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user, Role.STUDENT);
        });
        verify(userRepository, never()).save(any(User.class));
        verify(studentService, never()).saveStudent(any(Student.class));
    }

    @Test
    void registerUser_whenStudentWithEmailExists_shouldThrowException() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentService.findByEmail(anyString())).thenReturn(Optional.of(new Student()));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user, Role.STUDENT);
        });
        verify(userRepository, times(1)).save(any(User.class));
        verify(studentService, never()).saveStudent(any(Student.class));
    }

    @Test
    void registerUser_whenTeacherWithEmailExists_shouldThrowException() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherService.findByEmail(anyString())).thenReturn(Optional.of(new Teacher()));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user, Role.TEACHER);
        });
        verify(userRepository, times(1)).save(any(User.class));
        verify(teacherService, never()).saveTeacher(any(Teacher.class));
    }
}