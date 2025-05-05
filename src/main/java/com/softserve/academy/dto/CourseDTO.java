package com.softserve.academy.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object for Course entity.
 * Used to transfer course data between the controller and the view.
 */
public class CourseDTO {

    private Long id;

    @NotBlank(message = "Course name is required")
    private String name;

    private String description;

    private Long teacherId;
    
    private String teacherName;

    private Set<Long> studentIds = new HashSet<>();

    // Default constructor
    public CourseDTO() {
    }

    // Constructor with fields
    public CourseDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Constructor with all fields
    public CourseDTO(Long id, String name, String description, Long teacherId, String teacherName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Set<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(Set<Long> studentIds) {
        this.studentIds = studentIds;
    }
}