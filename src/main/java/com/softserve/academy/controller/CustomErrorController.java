package com.softserve.academy.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller to handle application errors and display appropriate error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handle error requests and display appropriate error pages.
     *
     * @param request the HTTP request
     * @param model the model for the view
     * @return the name of the error view to display
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        // Add error attributes to model
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode);
            
            // Add error message based on status code
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Not Found");
                model.addAttribute("message", "The page you are looking for does not exist or has been moved.");
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Forbidden");
                model.addAttribute("message", "You do not have permission to access this resource.");
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Internal Server Error");
                model.addAttribute("message", "Something went wrong on our servers.");
                return "error/500";
            }
        }
        
        // For other errors, use the generic error page
        model.addAttribute("error", "Error");
        model.addAttribute("message", "An unexpected error occurred.");
        return "error/error";
    }
}