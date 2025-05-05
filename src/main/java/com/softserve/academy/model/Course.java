package com.softserve.academy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a course in the educational system.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany(mappedBy = "enrolledCourses")
    @Builder.Default
    private Set<Student> students = new HashSet<>();

    // Constructor with fields
    public Course(String name, String description) {
        this.name = name;
        this.description = description;
        this.students = new HashSet<>();
    }

    // Helper methods

    /**
     * Adds a student to the course and sets the course for the student.
     *
     * @param student the student to add
     */
    public void addStudent(Student student) {
        this.students.add(student);
        student.getEnrolledCourses().add(this);
    }

    /**
     * Removes a student from the course and clears the course for the student.
     * @param student
     */
    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getEnrolledCourses().remove(this);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teacher=" + (teacher != null ? teacher.getName() : "None") +
                ", students=" + students.size() +
                '}';
    }
}
