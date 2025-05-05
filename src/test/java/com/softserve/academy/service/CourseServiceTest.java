package com.softserve.academy.service;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.repository.CourseRepository;
import com.softserve.academy.repository.StudentRepository;
import com.softserve.academy.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        course = new Course("Java Programming", "Introduction to Java programming language");
        course.setId(1L);

        teacher = new Teacher("John", "Smith", "john.smith@example.com");
        teacher.setId(1L);

        student = new Student("Jane", "Doe", "jane.doe@example.com");
        student.setId(1L);
    }

    @Test
    void getAllCourses_shouldReturnAllCourses() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(new Course("Python Programming", "Introduction to Python programming language"));
        when(courseRepository.findAll()).thenReturn(courses);

        // when
        List<Course> result = courseService.getAllCourses();

        // then
        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_whenCourseExists_shouldReturnCourse() {
        // given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // when
        Optional<Course> result = courseService.getCourseById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Java Programming");
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_whenCourseDoesNotExist_shouldReturnEmpty() {
        // given
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        // when
        Optional<Course> result = courseService.getCourseById(99L);

        // then
        assertThat(result).isEmpty();
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    void saveCourse_shouldSaveAndReturnCourse() {
        // given
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // when
        Course result = courseService.saveCourse(course);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Java Programming");
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void deleteCourse_shouldCallRepositoryDeleteById() {
        // given
        doNothing().when(courseRepository).deleteById(1L);

        // when
        courseService.deleteCourse(1L);

        // then
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByName_whenCourseExists_shouldReturnCourse() {
        // given
        when(courseRepository.findByName("Java Programming")).thenReturn(Optional.of(course));

        // when
        Optional<Course> result = courseService.findByName("Java Programming");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Java Programming");
        verify(courseRepository, times(1)).findByName("Java Programming");
    }

    @Test
    void assignTeacherToCourse_whenCourseAndTeacherExist_shouldAssignAndReturnCourse() {
        // given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // when
        Course result = courseService.assignTeacherToCourse(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(courseRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void assignTeacherToCourse_whenCourseDoesNotExist_shouldThrowException() {
        // given
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.assignTeacherToCourse(99L, 1L);
        });
        verify(courseRepository, times(1)).findById(99L);
        verify(teacherRepository, never()).findById(anyLong());
    }

    @Test
    void getEnrolledStudents_whenCourseExists_shouldReturnStudents() {
        // given
        Set<Student> students = new HashSet<>();
        students.add(student);
        course.setStudents(students);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // when
        Set<Student> result = courseService.getEnrolledStudents(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).contains(student);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void searchCourses_shouldReturnMatchingCourses() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        when(courseRepository.searchCourses("Java")).thenReturn(courses);

        // when
        List<Course> result = courseService.searchCourses("Java");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java Programming");
        verify(courseRepository, times(1)).searchCourses("Java");
    }

    @Test
    void getCoursesByTeacher_shouldReturnCourses() {
        // given
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        when(courseRepository.findByTeacherId(1L)).thenReturn(courses);

        // when
        List<Course> result = courseService.getCoursesByTeacher(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java Programming");
        verify(courseRepository, times(1)).findByTeacherId(1L);
    }

    @Test
    void addStudentToCourse_whenCourseAndStudentExist_shouldAddAndReturnCourse() {
        // given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // when
        Course result = courseService.addStudentToCourse(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void removeStudentFromCourse_whenCourseAndStudentExist_shouldRemoveAndReturnCourse() {
        // given
        course.setStudents(new HashSet<>());
        course.getStudents().add(student);
        student.getEnrolledCourses().add(course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // when
        Course result = courseService.removeStudentFromCourse(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }
}
