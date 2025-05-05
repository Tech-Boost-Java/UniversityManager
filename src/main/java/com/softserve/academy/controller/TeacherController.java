package com.softserve.academy.controller;

import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import com.softserve.academy.service.CourseService;
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

/**
 * Controller for handling teacher-related operations.
 */
@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;

    @Autowired
    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    /**
     * Display list of all teachers.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the teachers list view or redirect to login if not authenticated
     */
    @GetMapping
    public String listTeachers(Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "teachers/list";
    }

    /**
     * Display form to add a new teacher.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the teacher form view or redirect to login if not authenticated
     */
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        model.addAttribute("teacher", new Teacher());
        return "teachers/form";
    }

    /**
     * Display form to edit an existing teacher.
     *
     * @param id the teacher ID
     * @param model the model for the view
     * @param session the HTTP session
     * @return the teacher form view or redirect to login if not authenticated
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getTeacherById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + id));
        model.addAttribute("teacher", teacher);
        return "teachers/form";
    }

    /**
     * Process the teacher form submission.
     *
     * @param teacher the teacher from the form
     * @param result the binding result for validation
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to teachers list if successful
     */
    @PostMapping("/save")
    public String saveTeacher(@Valid Teacher teacher, BindingResult result, 
                             RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "teachers/form";
        }

        teacherService.saveTeacher(teacher);
        redirectAttributes.addFlashAttribute("success", "Teacher saved successfully!");
        return "redirect:/teachers";
    }

    /**
     * Delete a teacher.
     *
     * @param id the teacher ID to delete
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to teachers list
     */
    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, 
                               RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        teacherService.deleteTeacher(id);
        redirectAttributes.addFlashAttribute("success", "Teacher deleted successfully!");
        return "redirect:/teachers";
    }

    /**
     * Display teacher details including assigned courses.
     *
     * @param id the teacher ID
     * @param model the model for the view
     * @param session the HTTP session
     * @return the teacher details view
     */
    @GetMapping("/details/{id}")
    public String showTeacherDetails(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getTeacherById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + id));
        
        List<Course> assignedCourses = teacherService.getTeacherCourses(id);
        List<Course> availableCourses = courseService.getAllCourses();
        availableCourses.removeAll(assignedCourses);
        
        model.addAttribute("teacher", teacher);
        model.addAttribute("assignedCourses", assignedCourses);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("courseCount", teacherService.countCoursesByTeacher(id));
        
        return "teachers/details";
    }

    /**
     * Assign a course to a teacher.
     *
     * @param teacherId the teacher ID
     * @param courseId the course ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to teacher details
     */
    @PostMapping("/{teacherId}/assign-course")
    public String assignCourse(@PathVariable Long teacherId, 
                              @RequestParam Long courseId,
                              RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        teacherService.assignCourseToTeacher(teacherId, courseId);
        redirectAttributes.addFlashAttribute("success", "Course assigned to teacher successfully!");
        return "redirect:/teachers/details/" + teacherId;
    }

    /**
     * Remove a course from a teacher.
     *
     * @param teacherId the teacher ID
     * @param courseId the course ID
     * @param redirectAttributes for flash attributes
     * @param session the HTTP session
     * @return redirect to teacher details
     */
    @PostMapping("/{teacherId}/remove-course")
    public String removeCourse(@PathVariable Long teacherId, 
                              @RequestParam Long courseId,
                              RedirectAttributes redirectAttributes, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        teacherService.removeCourseFromTeacher(teacherId, courseId);
        redirectAttributes.addFlashAttribute("success", "Course removed from teacher successfully!");
        return "redirect:/teachers/details/" + teacherId;
    }

    /**
     * Search for teachers by name.
     *
     * @param name the name to search for
     * @param model the model for the view
     * @param session the HTTP session
     * @return the teachers list view with search results
     */
    @GetMapping("/search")
    public String searchTeachers(@RequestParam String name, Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        List<Teacher> teachers = teacherService.findByName(name);
        model.addAttribute("teachers", teachers);
        model.addAttribute("searchName", name);
        return "teachers/list";
    }
}