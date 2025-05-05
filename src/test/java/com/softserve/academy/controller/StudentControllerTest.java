package com.softserve.academy.controller;

import com.softserve.academy.dto.CourseDTO;
import com.softserve.academy.dto.StudentDTO;
import com.softserve.academy.mapper.CourseMapper;
import com.softserve.academy.mapper.StudentMapper;
import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.service.CourseService;
import com.softserve.academy.service.StudentService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private Student student;
    private StudentDTO studentDTO;
    private List<Student> students;
    private List<StudentDTO> studentDTOs;
    private Course course;
    private CourseDTO courseDTO;
    private List<Course> courses;
    private List<CourseDTO> courseDTOs;

    @BeforeEach
    void setUp() {
        // Set up view resolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        // Set up MockMvc with GlobalExceptionHandler
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setViewResolvers(viewResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Set up authenticated session
        session = new MockHttpSession();
        session.setAttribute("authenticated", true);
        session.setAttribute("username", "admin");

        // Set up test data
        student = new Student("John", "Doe", "john.doe@example.com");
        student.setId(1L);

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail("john.doe@example.com");

        students = new ArrayList<>();
        students.add(student);
        students.add(new Student("Jane", "Smith", "jane.smith@example.com"));

        studentDTOs = new ArrayList<>();
        studentDTOs.add(studentDTO);
        StudentDTO studentDTO2 = new StudentDTO();
        studentDTO2.setId(2L);
        studentDTO2.setFirstName("Jane");
        studentDTO2.setLastName("Smith");
        studentDTO2.setEmail("jane.smith@example.com");
        studentDTOs.add(studentDTO2);

        course = new Course("Java Programming", "Introduction to Java programming language");
        course.setId(1L);

        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setName("Java Programming");
        courseDTO.setDescription("Introduction to Java programming language");

        courses = new ArrayList<>();
        courses.add(course);

        courseDTOs = new ArrayList<>();
        courseDTOs.add(courseDTO);
    }

    @Test
    void listStudents_whenAuthenticated_shouldReturnStudentsList() throws Exception {
        // given
        when(studentService.getAllStudents()).thenReturn(students);
        when(studentMapper.toDTOList(students)).thenReturn(studentDTOs);

        // when & then
        mockMvc.perform(get("/students").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"))
                .andExpect(model().attribute("students", studentDTOs));
    }

    @Test
    void listStudents_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        // given
        MockHttpSession unauthenticatedSession = new MockHttpSession();

        // when & then
        mockMvc.perform(get("/students").session(unauthenticatedSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showAddForm_whenAuthenticated_shouldReturnStudentForm() throws Exception {
        // when & then
        mockMvc.perform(get("/students/add").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    void showEditForm_whenAuthenticated_shouldReturnStudentForm() throws Exception {
        // given
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        // when & then
        mockMvc.perform(get("/students/edit/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"))
                .andExpect(model().attribute("student", studentDTO));
    }

    @Test
    void showEditForm_whenStudentNotFound_shouldReturnNotFound() throws Exception {
        // given
        when(studentService.getStudentById(99L)).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/students/edit/99").session(session))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attribute("error", "Not Found"));
    }

    @Test
    void saveStudent_whenValidInput_shouldRedirectToStudentsList() throws Exception {
        // given
        when(studentMapper.toEntity(any(StudentDTO.class))).thenReturn(student);
        when(studentService.saveStudent(any(Student.class))).thenReturn(student);

        // when & then
        mockMvc.perform(post("/students/save").session(session)
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john.doe@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("success", "Student saved successfully!"));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    void deleteStudent_shouldRedirectToStudentsList() throws Exception {
        // given
        doNothing().when(studentService).deleteStudent(1L);

        // when & then
        mockMvc.perform(get("/students/delete/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attribute("success", "Student deleted successfully!"));

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void showStudentDetails_whenAuthenticated_shouldReturnStudentDetails() throws Exception {
        // given
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        when(studentService.getStudentCourses(1L)).thenReturn(courses);
        when(courseMapper.toDTOList(courses)).thenReturn(courseDTOs);
        when(courseService.getAllCourses()).thenReturn(new ArrayList<>());

        // Important: Be more specific with the matcher to avoid ambiguity
        when(courseMapper.toDTOList(argThat(list -> list != courses))).thenReturn(new ArrayList<>());

        // when & then
        mockMvc.perform(get("/students/details/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("students/details"))
                .andExpect(model().attribute("student", studentDTO))
                .andExpect(model().attribute("enrolledCourses", courseDTOs))
                .andExpect(model().attributeExists("availableCourses"));
    }

    @Test
    void enrollInCourse_shouldRedirectToStudentDetails() throws Exception {
        // given
        when(studentService.enrollStudentInCourse(1L, 1L)).thenReturn(student);

        // when & then
        mockMvc.perform(post("/students/1/enroll").session(session)
                .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/details/1"))
                .andExpect(flash().attribute("success", "Student enrolled in course successfully!"));

        verify(studentService, times(1)).enrollStudentInCourse(1L, 1L);
    }

    @Test
    void withdrawFromCourse_shouldRedirectToStudentDetails() throws Exception {
        // given
        when(studentService.withdrawStudentFromCourse(1L, 1L)).thenReturn(student);

        // when & then
        mockMvc.perform(post("/students/1/withdraw").session(session)
                .param("courseId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/details/1"))
                .andExpect(flash().attribute("success", "Student withdrawn from course successfully!"));

        verify(studentService, times(1)).withdrawStudentFromCourse(1L, 1L);
    }
}
