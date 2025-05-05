package com.softserve.academy.controller;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.service.CourseService;
import com.softserve.academy.service.StudentService;
import com.softserve.academy.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

/**
 * Controller for handling course-related operations.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    @Autowired
    public CourseController(CourseService courseService, TeacherService teacherService, StudentService studentService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    /**
     * Display list of all courses.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the courses list view or redirect to login if not authenticated
     */
    @GetMapping
    public String listCourses(Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    /**
     * Display form to add a new course.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the course form view or redirect to login if not authenticated
     */
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        model.addAttribute("course", new Course());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "courses/form";
    }

    /**
     * Display form to edit an existing course.
     *
     * @param id the course ID
     * @param model the model for the view
     * @param session the HTTP session
     * @return the course form view or redirect to login if not authenticated
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));
        model.addAttribute("course", course);
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "courses/form";
    }

    /**
     * Process the course form submission.
     *
     * @param course the course from the form
     * @param result the binding result for validation
     * @param teacherId the selected teacher ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to courses list if successful
     */
    @PostMapping("/save")
    public String saveCourse(@Valid Course course, BindingResult result, 
                            @RequestParam(required = false) Long teacherId,
                            RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "courses/form";
        }

        courseService.saveCourse(course);
        
        // Assign teacher if provided
        if (teacherId != null) {
            courseService.assignTeacherToCourse(course.getId(), teacherId);
        }
        
        redirectAttributes.addFlashAttribute("success", "Course saved successfully!");
        return "redirect:/courses";
    }

    /**
     * Delete a course.
     *
     * @param id the course ID to delete
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to courses list
     */
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, 
                              RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if the user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        return "redirect:/courses";
    }

    /**
     * Display course details including enrolled students and assigned teacher.
     *
     * @param id the course ID
     * @param model the model for the view
     * @param session the HTTP session
     * @return the course details view
     */
    @GetMapping("/details/{id}")
    public String showCourseDetails(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));
        
        Set<Student> enrolledStudents = courseService.getEnrolledStudents(id);
        List<Student> availableStudents = studentService.getAllStudents();
        availableStudents.removeAll(enrolledStudents);
        
        List<Teacher> availableTeachers = teacherService.getAllTeachers();
        
        model.addAttribute("course", course);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("availableStudents", availableStudents);
        model.addAttribute("availableTeachers", availableTeachers);
        
        return "courses/details";
    }

    /**
     * Assign a teacher to a course.
     *
     * @param courseId the course ID
     * @param teacherId the teacher ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to course details
     */
    @PostMapping("/{courseId}/assign-teacher")
    public String assignTeacher(@PathVariable Long courseId, 
                               @RequestParam Long teacherId,
                               RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        courseService.assignTeacherToCourse(courseId, teacherId);
        redirectAttributes.addFlashAttribute("success", "Teacher assigned to course successfully!");
        return "redirect:/courses/details/" + courseId;
    }

    /**
     * Add a student to a course.
     *
     * @param courseId the course ID
     * @param studentId the student ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to course details
     */
    @PostMapping("/{courseId}/add-student")
    public String addStudent(@PathVariable Long courseId, 
                            @RequestParam Long studentId,
                            RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        courseService.addStudentToCourse(courseId, studentId);
        redirectAttributes.addFlashAttribute("success", "Student added to course successfully!");
        return "redirect:/courses/details/" + courseId;
    }

    /**
     * Remove a student from a course.
     *
     * @param courseId the course ID
     * @param studentId the student ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to course details
     */
    @PostMapping("/{courseId}/remove-student")
    public String removeStudent(@PathVariable Long courseId, 
                               @RequestParam Long studentId,
                               RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        courseService.removeStudentFromCourse(courseId, studentId);
        redirectAttributes.addFlashAttribute("success", "Student removed from course successfully!");
        return "redirect:/courses/details/" + courseId;
    }

    /**
     * Search for courses.
     *
     * @param searchText the text to search for
     * @param model the model for the view
     * @param session the HTTP session
     * @return the courses list view with search results
     */
    @GetMapping("/search")
    public String searchCourses(@RequestParam String searchText, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        List<Course> courses = courseService.searchCourses(searchText);
        model.addAttribute("courses", courses);
        model.addAttribute("searchText", searchText);
        return "courses/list";
    }
}