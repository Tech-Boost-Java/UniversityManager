package com.softserve.academy.service;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.repository.CourseRepository;
import com.softserve.academy.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student("John", "Doe", "john.doe@example.com");
        student.setId(1L);
        
        course = new Course("Java Programming", "Introduction to Java programming language");
        course.setId(1L);
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        // given
        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(new Student("Jane", "Doe", "jane.doe@example.com"));
        when(studentRepository.findAll()).thenReturn(students);

        // when
        List<Student> result = studentService.getAllStudents();

        // then
        assertThat(result).hasSize(2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_whenStudentExists_shouldReturnStudent() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // when
        Optional<Student> result = studentService.getStudentById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getStudentById_whenStudentDoesNotExist_shouldReturnEmpty() {
        // given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // when
        Optional<Student> result = studentService.getStudentById(99L);

        // then
        assertThat(result).isEmpty();
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void saveStudent_shouldSaveAndReturnStudent() {
        // given
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = studentService.saveStudent(student);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void deleteStudent_shouldCallRepositoryDeleteById() {
        // given
        doNothing().when(studentRepository).deleteById(1L);

        // when
        studentService.deleteStudent(1L);

        // then
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByEmail_whenStudentExists_shouldReturnStudent() {
        // given
        when(studentRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(student));

        // when
        Optional<Student> result = studentService.findByEmail("john.doe@example.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        verify(studentRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void enrollStudentInCourse_whenStudentAndCourseExist_shouldEnrollAndReturnStudent() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = studentService.enrollStudentInCourse(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void enrollStudentInCourse_whenStudentDoesNotExist_shouldThrowException() {
        // given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.enrollStudentInCourse(99L, 1L);
        });
        verify(studentRepository, times(1)).findById(99L);
        verify(courseRepository, never()).findById(anyLong());
    }

    @Test
    void withdrawStudentFromCourse_whenStudentAndCourseExist_shouldWithdrawAndReturnStudent() {
        // given
        student.setEnrolledCourses(new HashSet<>());
        student.getEnrolledCourses().add(course);
        course.getStudents().add(student);
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = studentService.withdrawStudentFromCourse(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void getStudentCourses_whenStudentExists_shouldReturnCourses() {
        // given
        student.setEnrolledCourses(new HashSet<>());
        student.getEnrolledCourses().add(course);
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // when
        List<Course> result = studentService.getStudentCourses(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java Programming");
        verify(studentRepository, times(1)).findById(1L);
    }
}