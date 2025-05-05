package com.softserve.academy.controller;

import com.softserve.academy.model.Role;
import com.softserve.academy.model.User;
import com.softserve.academy.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller for handling login, registration, and authentication.
 */
@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;

        // Create admin user if it doesn't exist
        if (!userService.usernameExists("admin")) {
            User adminUser = new User("admin", "admin123", "admin@example.com", "Admin", Role.ADMIN);
            userService.registerUser(adminUser);
        }
    }

    /**
     * Display the login page.
     *
     * @return the login view name
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Display the registration page.
     *
     * @param model the model for the view
     * @return the registration view name
     */
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Process registration form submission.
     *
     * @param user the user from the form
     * @param result the binding result for validation
     * @param redirectAttributes for flash attributes
     * @return redirect to login if successful, otherwise back to registration form
     */
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") User user,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        // Check for validation errors
        if (result.hasErrors()) {
            return "register";
        }

        try {
            // Register the user with the selected role
            userService.registerUser(user, user.getRole());
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Add error message and return to registration form
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Process login form submission.
     *
     * @param username the username from the form
     * @param password the password from the form
     * @param session the HTTP session
     * @param redirectAttributes for flash attributes
     * @return redirect to dashboard if successful, otherwise back to login
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = userService.authenticate(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Store user information in session
            session.setAttribute("username", user.getUsername());
            session.setAttribute("authenticated", true);
            session.setAttribute("userId", user.getId());

            return "redirect:/dashboard";
        } else {
            // Add error message and redirect back to login
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    /**
     * Display the dashboard/welcome page.
     *
     * @param model the model for the view
     * @param session the HTTP session
     * @return the dashboard view or redirect to login if not authenticated
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Check if user is authenticated
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        if (authenticated != null && authenticated) {
            String username = (String) session.getAttribute("username");
            model.addAttribute("username", username);
            return "dashboard";
        } else {
            // Redirect to login if not authenticated
            return "redirect:/login";
        }
    }

    /**
     * Process logout request.
     *
     * @param session the HTTP session
     * @return redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate session
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Redirect root URL to login page.
     *
     * @return redirect to login page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}
