package com.softserve.academy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Handles exceptions thrown by controllers and returns appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle IllegalArgumentException and return 404 Not Found status code.
     * This is typically used when a resource is not found.
     *
     * @param ex the exception
     * @return the model and view
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Resource not found: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", "Not Found");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    /**
     * Handle validation exceptions and return 400 Bad Request status code.
     * This is used when input validation fails.
     *
     * @param ex the exception
     * @return the model and view
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException ex) {
        logger.error("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Bad Request");
        modelAndView.addObject("message", "The submitted form contains invalid data. Please check your input and try again.");
        modelAndView.addObject("validationErrors", errors);
        return modelAndView;
    }

    /**
     * Handle database integrity violation exceptions and return 409 Conflict status code.
     * This is used when a database constraint is violated (e.g., unique constraint).
     *
     * @param ex the exception
     * @return the model and view
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("status", HttpStatus.CONFLICT.value());
        modelAndView.addObject("error", "Conflict");
        modelAndView.addObject("message", "The operation could not be completed due to a conflict with existing data. This might be caused by a duplicate entry or a constraint violation.");
        return modelAndView;
    }

    /**
     * Handle database access exceptions and return 500 Internal Server Error status code.
     * This is used when there's an issue with database access.
     *
     * @param ex the exception
     * @return the model and view
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleDataAccessException(DataAccessException ex) {
        logger.error("Database error: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("error", "Database Error");
        modelAndView.addObject("message", "There was an error accessing the database. Please try again later.");
        return modelAndView;
    }

    /**
     * Handle general exceptions and return 500 Internal Server Error status code.
     *
     * @param ex the exception
     * @return the model and view
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("error", "Internal Server Error");
        modelAndView.addObject("message", "Something went wrong on our servers. Our technical team has been notified.");
        return modelAndView;
    }
}
