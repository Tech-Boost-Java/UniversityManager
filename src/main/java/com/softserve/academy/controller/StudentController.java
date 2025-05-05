package com.softserve.academy.controller;

import com.softserve.academy.dto.CourseDTO;
import com.softserve.academy.dto.StudentDTO;
import com.softserve.academy.mapper.CourseMapper;
import com.softserve.academy.mapper.StudentMapper;
import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.service.CourseService;
import com.softserve.academy.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for handling student-related operations.
 */
@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Autowired
    public StudentController(StudentService studentService, CourseService courseService,
                            StudentMapper studentMapper, CourseMapper courseMapper) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
    }

    /**
     * Display a list of all students.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the students list view or redirect to login if not authenticated
     */
    @GetMapping
    public String listStudents(Model model, HttpSession session) {
        // Check if the user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        List<Student> students = studentService.getAllStudents();
        List<StudentDTO> studentDTOs = studentMapper.toDTOList(students);
        model.addAttribute("students", studentDTOs);
        return "students/list";
    }

    /**
     * Display form to add a new student.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the student form view or redirect to login if not authenticated
     */
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        model.addAttribute("student", new StudentDTO());
        return "students/form";
    }

    /**
     * Display form to edit an existing student.
     *
     * @param id the student ID
     * @param model the model for the view
     * @param session the HTTP session
     * @return the student form view or redirect to login if not authenticated
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));
        StudentDTO studentDTO = studentMapper.toDTO(student);
        model.addAttribute("student", studentDTO);
        return "students/form";
    }

    /**
     * Process the student form submission.
     *
     * @param studentDTO the student DTO from the form
     * @param result the binding result for validation
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to students list if successful
     */
    @PostMapping("/save")
    public String saveStudent(@Valid StudentDTO studentDTO, BindingResult result, 
                             RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "students/form";
        }

        Student student = studentMapper.toEntity(studentDTO);
        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("success", "Student saved successfully!");
        return "redirect:/students";
    }

    /**
     * Delete a student.
     *
     * @param id the student ID to delete
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to students list
     */
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, 
                               RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        return "redirect:/students";
    }

    /**
     * Display student details including enrolled courses.
     *
     * @param id the student ID
     * @param model the model for the view
     * @param session the HTTP session
     * @return the student details view
     */
    @GetMapping("/details/{id}")
    public String showStudentDetails(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));
        StudentDTO studentDTO = studentMapper.toDTO(student);

        List<Course> enrolledCourses = studentService.getStudentCourses(id);
        List<CourseDTO> enrolledCourseDTOs = courseMapper.toDTOList(enrolledCourses);

        List<Course> availableCourses = courseService.getAllCourses();
        availableCourses.removeAll(enrolledCourses);
        List<CourseDTO> availableCourseDTOs = courseMapper.toDTOList(availableCourses);

        model.addAttribute("student", studentDTO);
        model.addAttribute("enrolledCourses", enrolledCourseDTOs);
        model.addAttribute("availableCourses", availableCourseDTOs);

        return "students/details";
    }

    /**
     * Enroll a student in a course.
     *
     * @param studentId the student ID
     * @param courseId the course ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to student details
     */
    @PostMapping("/{studentId}/enroll")
    public String enrollInCourse(@PathVariable Long studentId, 
                                @RequestParam Long courseId,
                                RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        studentService.enrollStudentInCourse(studentId, courseId);
        redirectAttributes.addFlashAttribute("success", "Student enrolled in course successfully!");
        return "redirect:/students/details/" + studentId;
    }

    /**
     * Withdraw a student from a course.
     *
     * @param studentId the student ID
     * @param courseId the course ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to student details
     */
    @PostMapping("/{studentId}/withdraw")
    public String withdrawFromCourse(@PathVariable Long studentId, 
                                    @RequestParam Long courseId,
                                    RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        studentService.withdrawStudentFromCourse(studentId, courseId);
        redirectAttributes.addFlashAttribute("success", "Student withdrawn from course successfully!");
        return "redirect:/students/details/" + studentId;
    }
}
