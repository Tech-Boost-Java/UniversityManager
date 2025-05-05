package com.softserve.academy.mapper;

import com.softserve.academy.dto.CourseDTO;
import com.softserve.academy.model.Course;
import com.softserve.academy.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Course entity and CourseDTO.
 */
@Component
public class CourseMapper {

    /**
     * Convert Course entity to CourseDTO.
     *
     * @param course the Course entity
     * @return the CourseDTO
     */
    public CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseDTO dto = new CourseDTO(
                course.getId(),
                course.getName(),
                course.getDescription()
        );

        // Set teacher information if available
        if (course.getTeacher() != null) {
            dto.setTeacherId(course.getTeacher().getId());
            dto.setTeacherName(course.getTeacher().getName());
        }

        // Map enrolled students to their IDs
        if (course.getStudents() != null) {
            Set<Long> studentIds = course.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            dto.setStudentIds(studentIds);
        }

        return dto;
    }

    /**
     * Convert CourseDTO to Course entity.
     * Note: This does not set the teacher or students fields, as that requires
     * fetching the actual entities from the database.
     *
     * @param dto the CourseDTO
     * @return the Course entity
     */
    public Course toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }

        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());

        return course;
    }

    /**
     * Convert a list of Course entities to a list of CourseDTOs.
     *
     * @param courses the list of Course entities
     * @return the list of CourseDTOs
     */
    public List<CourseDTO> toDTOList(List<Course> courses) {
        if (courses == null) {
            return null;
        }

        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}