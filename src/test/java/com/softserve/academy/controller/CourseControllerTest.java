package com.softserve.academy.controller;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.service.CourseService;
import com.softserve.academy.service.StudentService;
import com.softserve.academy.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private Course course;
    private List<Course> courses;
    private Teacher teacher;
    private List<Teacher> teachers;
    private Student student;
    private Set<Student> students;
    private List<Student> studentList;

    @BeforeEach
    void setUp() {
        // Set up view resolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        // Set up MockMvc with GlobalExceptionHandler
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setViewResolvers(viewResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Set up authenticated session
        session = new MockHttpSession();
        session.setAttribute("authenticated", true);
        session.setAttribute("username", "admin");

        // Set up test data
        course = new Course("Java Programming", "Introduction to Java programming language");
        course.setId(1L);

        courses = new ArrayList<>();
        courses.add(course);
        courses.add(new Course("Python Programming", "Introduction to Python programming language"));

        teacher = new Teacher("John", "Smith", "john.smith@example.com");
        teacher.setId(1L);
        course.setTeacher(teacher);

        teachers = new ArrayList<>();
        teachers.add(teacher);
        teachers.add(new Teacher("Jane", "Doe", "jane.doe@example.com"));

        student = new Student("Alice", "Johnson", "alice.johnson@example.com");
        student.setId(1L);

        students = new HashSet<>();
        students.add(student);

        studentList = new ArrayList<>();
        studentList.add(student);
        studentList.add(new Student("Bob", "Brown", "bob.brown@example.com"));
    }

    @Test
    void listCourses_whenAuthenticated_shouldReturnCoursesList() throws Exception {
        // given
        when(courseService.getAllCourses()).thenReturn(courses);

        // when & then
        mockMvc.perform(get("/courses").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/list"))
                .andExpect(model().attribute("courses", courses));
    }

    @Test
    void listCourses_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        // given
        MockHttpSession unauthenticatedSession = new MockHttpSession();

        // when & then
        mockMvc.perform(get("/courses").session(unauthenticatedSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showAddForm_whenAuthenticated_shouldReturnCourseForm() throws Exception {
        // given
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        // when & then
        mockMvc.perform(get("/courses/add").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("teachers", teachers));
    }

    @Test
    void showEditForm_whenAuthenticated_shouldReturnCourseForm() throws Exception {
        // given
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        // when & then
        mockMvc.perform(get("/courses/edit/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"))
                .andExpect(model().attribute("course", course))
                .andExpect(model().attribute("teachers", teachers));
    }

    @Test
    void showEditForm_whenCourseNotFound_shouldReturnNotFound() throws Exception {
        // given
        when(courseService.getCourseById(99L)).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/courses/edit/99").session(session))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("error", "Not Found"));
    }

    @Test
    void saveCourse_whenValidInput_shouldRedirectToCoursesList() throws Exception {
        // given
        when(courseService.saveCourse(any(Course.class))).thenReturn(course);

        // Use doReturn for more lenient argument matching
        doReturn(course).when(courseService).assignTeacherToCourse(any(), any());

        // when & then
        mockMvc.perform(post("/courses/save").session(session)
                .param("name", "Java Programming")
                .param("description", "Introduction to Java programming language")
                .param("teacherId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("success", "Course saved successfully!"));

        verify(courseService, times(1)).saveCourse(any(Course.class));
        verify(courseService, times(1)).assignTeacherToCourse(any(), any());
    }

    @Test
    void saveCourse_withoutTeacher_shouldRedirectToCoursesList() throws Exception {
        // given
        when(courseService.saveCourse(any(Course.class))).thenReturn(course);

        // when & then
        mockMvc.perform(post("/courses/save").session(session)
                .param("name", "Java Programming")
                .param("description", "Introduction to Java programming language"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("success", "Course saved successfully!"));

        verify(courseService, times(1)).saveCourse(any(Course.class));
        verify(courseService, never()).assignTeacherToCourse(anyLong(), anyLong());
    }

    @Test
    void deleteCourse_shouldRedirectToCoursesList() throws Exception {
        // given
        doNothing().when(courseService).deleteCourse(1L);

        // when & then
        mockMvc.perform(get("/courses/delete/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attribute("success", "Course deleted successfully!"));

        verify(courseService, times(1)).deleteCourse(1L);
    }

    @Test
    void showCourseDetails_whenAuthenticated_shouldReturnCourseDetails() throws Exception {
        // given
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(courseService.getEnrolledStudents(1L)).thenReturn(students);
        when(studentService.getAllStudents()).thenReturn(studentList);
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        // when & then
        mockMvc.perform(get("/courses/details/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/details"))
                .andExpect(model().attribute("course", course))
                .andExpect(model().attribute("enrolledStudents", students))
                .andExpect(model().attributeExists("availableStudents"))
                .andExpect(model().attribute("availableTeachers", teachers));
    }

    @Test
    void assignTeacher_shouldRedirectToCourseDetails() throws Exception {
        // given
        when(courseService.assignTeacherToCourse(1L, 1L)).thenReturn(course);

        // when & then
        mockMvc.perform(post("/courses/1/assign-teacher").session(session)
                .param("teacherId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/details/1"))
                .andExpect(flash().attribute("success", "Teacher assigned to course successfully!"));

        verify(courseService, times(1)).assignTeacherToCourse(1L, 1L);
    }

    @Test
    void addStudent_shouldRedirectToCourseDetails() throws Exception {
        // given
        when(courseService.addStudentToCourse(1L, 1L)).thenReturn(course);

        // when & then
        mockMvc.perform(post("/courses/1/add-student").session(session)
                .param("studentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/details/1"))
                .andExpect(flash().attribute("success", "Student added to course successfully!"));

        verify(courseService, times(1)).addStudentToCourse(1L, 1L);
    }

    @Test
    void removeStudent_shouldRedirectToCourseDetails() throws Exception {
        // given
        when(courseService.removeStudentFromCourse(1L, 1L)).thenReturn(course);

        // when & then
        mockMvc.perform(post("/courses/1/remove-student").session(session)
                .param("studentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/details/1"))
                .andExpect(flash().attribute("success", "Student removed from course successfully!"));

        verify(courseService, times(1)).removeStudentFromCourse(1L, 1L);
    }

    @Test
    void searchCourses_shouldReturnCoursesList() throws Exception {
        // given
        when(courseService.searchCourses("Java")).thenReturn(courses);

        // when & then
        mockMvc.perform(get("/courses/search").session(session)
                .param("searchText", "Java"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/list"))
                .andExpect(model().attribute("courses", courses))
                .andExpect(model().attribute("searchText", "Java"));

        verify(courseService, times(1)).searchCourses("Java");
    }
}
