package com.softserve.academy.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.softserve.academy.model.Teacher;

/**
 * Test controller for demonstrating error pages.
 * This controller is for testing purposes only and should be removed in production.
 */
@Controller
@RequestMapping("/test-error")
public class TestErrorController {

    /**
     * Trigger a 404 Not Found error.
     *
     * @return never returns as it throws an exception
     */
    @GetMapping("/404")
    public String trigger404() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
    }

    /**
     * Trigger a 403 Forbidden error.
     *
     * @return never returns as it throws an exception
     */
    @GetMapping("/403")
    public String trigger403() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    /**
     * Trigger a 500 Internal Server Error.
     *
     * @return never returns as it throws an exception
     */
    @GetMapping("/500")
    public String trigger500() {
        throw new RuntimeException("Simulated server error");
    }

    /**
     * Trigger a generic error.
     *
     * @return never returns as it throws an exception
     */
    @GetMapping("/error")
    public String triggerError() {
        throw new IllegalStateException("Simulated application error");
    }

    /**
     * Test page for validation errors.
     *
     * @return the view name
     */
    @GetMapping("/validation")
    public String showValidationForm(@ModelAttribute Teacher teacher) {
        return "test-validation";
    }

    /**
     * Process form submission with validation.
     * This will trigger a 400 Bad Request if validation fails.
     *
     * @param teacher the teacher from the form
     * @param result the binding result for validation
     * @return the view name
     */
    @PostMapping("/validation")
    public String processValidationForm(@Valid @ModelAttribute Teacher teacher, BindingResult result) {
        if (result.hasErrors()) {
            // This will be handled by the GlobalExceptionHandler
            throw new IllegalArgumentException("Validation failed");
        }
        return "redirect:/test-error/validation?success";
    }
}
