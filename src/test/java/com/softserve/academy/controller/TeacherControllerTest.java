package com.softserve.academy.controller;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.service.CourseService;
import com.softserve.academy.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private Teacher teacher;
    private List<Teacher> teachers;
    private Course course;
    private List<Course> courses;

    @BeforeEach
    void setUp() {
        // Set up view resolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        // Set up MockMvc with GlobalExceptionHandler
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setViewResolvers(viewResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Set up authenticated session
        session = new MockHttpSession();
        session.setAttribute("authenticated", true);
        session.setAttribute("username", "admin");

        // Set up test data
        teacher = new Teacher("John", "Smith", "john.smith@example.com");
        teacher.setId(1L);

        teachers = new ArrayList<>();
        teachers.add(teacher);
        teachers.add(new Teacher("Jane", "Doe", "jane.doe@example.com"));

        course = new Course("Java Programming", "Introduction to Java programming language");
        course.setId(1L);

        courses = new ArrayList<>();
        courses.add(course);
    }

    @Test
    void listTeachers_whenAuthenticated_shouldReturnTeachersList() throws Exception {
        // given
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        // when & then
        mockMvc.perform(get("/teachers").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/list"))
                .andExpect(model().attribute("teachers", teachers));
    }

    @Test
    void listTeachers_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        // given
        MockHttpSession unauthenticatedSession = new MockHttpSession();

        // when & then
        mockMvc.perform(get("/teachers").session(unauthenticatedSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showAddForm_whenAuthenticated_shouldReturnTeacherForm() throws Exception {
        // when & then
        mockMvc.perform(get("/teachers/add").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/form"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    void showEditForm_whenAuthenticated_shouldReturnTeacherForm() throws Exception {
        // given
        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(teacher));

        // when & then
        mockMvc.perform(get("/teachers/edit/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/form"))
                .andExpect(model().attribute("teacher", teacher));
    }

    @Test
    void showEditForm_whenTeacherNotFound_shouldReturnNotFound() throws Exception {
        // given
        when(teacherService.getTeacherById(99L)).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/teachers/edit/99").session(session))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("error", "Not Found"));
    }

    @Test
    void saveTeacher_whenValidInput_shouldRedirectToTeachersList() throws Exception {
        // given
        when(teacherService.saveTeacher(any(Teacher.class))).thenReturn(teacher);

        // when & then
        mockMvc.perform(post("/teachers/save").session(session)
                .param("firstName", "John")
                .param("lastName", "Smith")
                .param("email", "john.smith@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("success", "Teacher saved successfully!"));

        verify(teacherService, times(1)).saveTeacher(any(Teacher.class));
    }

    @Test
    void deleteTeacher_shouldRedirectToTeachersList() throws Exception {
        // given
        doNothing().when(teacherService).deleteTeacher(1L);

        // when & then
        mockMvc.perform(get("/teachers/delete/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(flash().attribute("success", "Teacher deleted successfully!"));

        verify(teacherService, times(1)).deleteTeacher(1L);
    }

    @Test
    void showTeacherDetails_whenAuthenticated_shouldReturnTeacherDetails() throws Exception {
        // given
        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(teacher));
        when(teacherService.getTeacherCourses(1L)).thenReturn(courses);
        when(courseService.getAllCourses()).thenReturn(new ArrayList<>());
        when(teacherService.countCoursesByTeacher(1L)).thenReturn(1L);

        // when & then
        mockMvc.perform(get("/teachers/details/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/details"))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attribute("assignedCourses", courses))
                .andExpect(model().attributeExists("availableCourses"))
                .andExpect(model().attribute("courseCount", 1L));
    }

    @Test
    void assignCourse_shouldRedirectToTeacherDetails() throws Exception {
        // given
        when(teacherService.assignCourseToTeacher(1L, 1L)).thenReturn(teacher);

        // when & then
        mockMvc.perform(post("/teachers/1/assign-course").session(session)
                .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers/details/1"))
                .andExpect(flash().attribute("success", "Course assigned to teacher successfully!"));

        verify(teacherService, times(1)).assignCourseToTeacher(1L, 1L);
    }

    @Test
    void removeCourse_shouldRedirectToTeacherDetails() throws Exception {
        // given
        when(teacherService.removeCourseFromTeacher(1L, 1L)).thenReturn(teacher);

        // when & then
        mockMvc.perform(post("/teachers/1/remove-course").session(session)
                .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers/details/1"))
                .andExpect(flash().attribute("success", "Course removed from teacher successfully!"));

        verify(teacherService, times(1)).removeCourseFromTeacher(1L, 1L);
    }

    @Test
    void searchTeachers_shouldReturnTeachersList() throws Exception {
        // given
        when(teacherService.findByName("Smith")).thenReturn(teachers);

        // when & then
        mockMvc.perform(get("/teachers/search").session(session)
                .param("name", "Smith"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/list"))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("searchName", "Smith"));

        verify(teacherService, times(1)).findByName("Smith");
    }

    @Test
    void saveTeacher_whenValidationFails_shouldReturnFormView() throws Exception {
        // when & then
        mockMvc.perform(post("/teachers/save").session(session)
                .param("firstName", "") // Empty first name to trigger validation error
                .param("lastName", "Smith")
                .param("email", "invalid-email")) // Invalid email to trigger validation error
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/form"))
                .andExpect(model().attributeHasFieldErrors("teacher", "firstName", "email"));

        verify(teacherService, never()).saveTeacher(any(Teacher.class));
    }

    @Test
    void deleteTeacher_whenServiceThrowsException_shouldReturnNotFound() throws Exception {
        // given
        doThrow(new IllegalArgumentException("Teacher not found with ID: 99"))
                .when(teacherService).deleteTeacher(99L);

        // when & then
        mockMvc.perform(get("/teachers/delete/99").session(session))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("error", "Not Found"))
                .andExpect(model().attribute("message", "Teacher not found with ID: 99"));
    }

    @Test
    void assignCourse_whenServiceThrowsException_shouldReturnNotFound() throws Exception {
        // given
        doThrow(new IllegalArgumentException("Course not found with ID: 99"))
                .when(teacherService).assignCourseToTeacher(1L, 99L);

        // when & then
        mockMvc.perform(post("/teachers/1/assign-course").session(session)
                .param("courseId", "99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("error", "Not Found"))
                .andExpect(model().attribute("message", "Course not found with ID: 99"));
    }

    @Test
    void removeCourse_whenServiceThrowsException_shouldReturnNotFound() throws Exception {
        // given
        doThrow(new IllegalArgumentException("Course not found with ID: 99"))
                .when(teacherService).removeCourseFromTeacher(1L, 99L);

        // when & then
        mockMvc.perform(post("/teachers/1/remove-course").session(session)
                .param("courseId", "99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("error", "Not Found"))
                .andExpect(model().attribute("message", "Course not found with ID: 99"));
    }

    @Test
    void saveTeacher_whenDataIntegrityViolation_shouldReturnConflict() throws Exception {
        // given
        when(teacherService.saveTeacher(any(Teacher.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email"));

        // when & then
        mockMvc.perform(post("/teachers/save").session(session)
                .param("firstName", "John")
                .param("lastName", "Smith")
                .param("email", "john.smith@example.com"))
                .andExpect(status().isConflict())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("status", 409))
                .andExpect(model().attribute("error", "Conflict"));
    }
}
