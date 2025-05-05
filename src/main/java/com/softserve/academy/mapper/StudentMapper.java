package com.softserve.academy.mapper;

import com.softserve.academy.dto.StudentDTO;
import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Student entity and StudentDTO.
 */
@Component
public class StudentMapper {

    /**
     * Convert Student entity to StudentDTO.
     *
     * @param student the Student entity
     * @return the StudentDTO
     */
    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );

        // Map enrolled courses to their IDs
        if (student.getEnrolledCourses() != null) {
            Set<Long> courseIds = student.getEnrolledCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toSet());
            dto.setEnrolledCourseIds(courseIds);
        }

        return dto;
    }

    /**
     * Convert StudentDTO to Student entity.
     * Note: This does not set the enrolledCourses field, as that requires
     * fetching the actual Course entities from the database.
     *
     * @param dto the StudentDTO
     * @return the Student entity
     */
    public Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }

        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());

        return student;
    }

    /**
     * Convert a list of Student entities to a list of StudentDTOs.
     *
     * @param students the list of Student entities
     * @return the list of StudentDTOs
     */
    public List<StudentDTO> toDTOList(List<Student> students) {
        if (students == null) {
            return null;
        }

        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}