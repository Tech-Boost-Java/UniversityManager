package com.softserve.academy.service;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.repository.CourseRepository;
import com.softserve.academy.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("John", "Smith", "john.smith@example.com");
        teacher.setId(1L);

        course = new Course("Java Programming", "Introduction to Java programming language");
        course.setId(1L);
    }

    @Test
    void getAllTeachers_shouldReturnAllTeachers() {
        // given
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        teachers.add(new Teacher("Jane", "Doe", "jane.doe@example.com"));
        when(teacherRepository.findAll()).thenReturn(teachers);

        // when
        List<Teacher> result = teacherService.getAllTeachers();

        // then
        assertThat(result).hasSize(2);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void getTeacherById_whenTeacherExists_shouldReturnTeacher() {
        // given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // when
        Optional<Teacher> result = teacherService.getTeacherById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Smith");
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void getTeacherById_whenTeacherDoesNotExist_shouldReturnEmpty() {
        // given
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        // when
        Optional<Teacher> result = teacherService.getTeacherById(99L);

        // then
        assertThat(result).isEmpty();
        verify(teacherRepository, times(1)).findById(99L);
    }

    @Test
    void saveTeacher_shouldSaveAndReturnTeacher() {
        // given
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        // when
        Teacher result = teacherService.saveTeacher(teacher);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Smith");
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void deleteTeacher_whenTeacherExists_shouldDeleteTeacher() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        teacher.setCourses(courses);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        doNothing().when(teacherRepository).deleteById(1L);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // when
        teacherService.deleteTeacher(1L);

        // then
        verify(teacherRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTeacher_whenTeacherDoesNotExist_shouldThrowException() {
        // given
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.deleteTeacher(99L);
        });
        verify(teacherRepository, times(1)).findById(99L);
        verify(teacherRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByEmail_whenTeacherExists_shouldReturnTeacher() {
        // given
        when(teacherRepository.findByEmail("john.smith@example.com")).thenReturn(Optional.of(teacher));

        // when
        Optional<Teacher> result = teacherService.findByEmail("john.smith@example.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.smith@example.com");
        verify(teacherRepository, times(1)).findByEmail("john.smith@example.com");
    }

    @Test
    void findByName_shouldReturnMatchingTeachers() {
        // given
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        when(teacherRepository.findByNameContainingIgnoreCase("Smith")).thenReturn(teachers);

        // when
        List<Teacher> result = teacherService.findByName("Smith");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Smith");
        verify(teacherRepository, times(1)).findByNameContainingIgnoreCase("Smith");
    }

    @Test
    void getTeacherCourses_shouldReturnCourses() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        when(courseRepository.findByTeacherId(1L)).thenReturn(courses);

        // when
        List<Course> result = teacherService.getTeacherCourses(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java Programming");
        verify(courseRepository, times(1)).findByTeacherId(1L);
    }

    @Test
    void countCoursesByTeacher_shouldReturnCount() {
        // given
        when(teacherRepository.countCoursesByTeacherId(1L)).thenReturn(2L);

        // when
        Long result = teacherService.countCoursesByTeacher(1L);

        // then
        assertThat(result).isEqualTo(2L);
        verify(teacherRepository, times(1)).countCoursesByTeacherId(1L);
    }

    @Test
    void assignCourseToTeacher_whenTeacherAndCourseExist_shouldAssignAndReturnTeacher() {
        // given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        // when
        Teacher result = teacherService.assignCourseToTeacher(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(teacherRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void assignCourseToTeacher_whenTeacherDoesNotExist_shouldThrowException() {
        // given
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.assignCourseToTeacher(99L, 1L);
        });
        verify(teacherRepository, times(1)).findById(99L);
        verify(courseRepository, never()).findById(anyLong());
    }

    @Test
    void removeCourseFromTeacher_whenTeacherAndCourseExist_shouldRemoveAndReturnTeacher() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        teacher.setCourses(courses);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        // when
        Teacher result = teacherService.removeCourseFromTeacher(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(teacherRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(teacher);
    }
}
