package com.softserve.academy.mapper;

import com.softserve.academy.dto.TeacherDTO;
import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Teacher entity and TeacherDTO.
 */
@Component
public class TeacherMapper {

    /**
     * Convert Teacher entity to TeacherDTO.
     *
     * @param teacher the Teacher entity
     * @return the TeacherDTO
     */
    public TeacherDTO toDTO(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        TeacherDTO dto = new TeacherDTO(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail()
        );

        // Map courses to their IDs
        if (teacher.getCourses() != null) {
            List<Long> courseIds = teacher.getCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toList());
            dto.setCourseIds(courseIds);
        }

        return dto;
    }

    /**
     * Convert TeacherDTO to Teacher entity.
     * Note: This does not set the courses field, as that requires
     * fetching the actual Course entities from the database.
     *
     * @param dto the TeacherDTO
     * @return the Teacher entity
     */
    public Teacher toEntity(TeacherDTO dto) {
        if (dto == null) {
            return null;
        }

        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setEmail(dto.getEmail());

        return teacher;
    }

    /**
     * Convert a list of Teacher entities to a list of TeacherDTOs.
     *
     * @param teachers the list of Teacher entities
     * @return the list of TeacherDTOs
     */
    public List<TeacherDTO> toDTOList(List<Teacher> teachers) {
        if (teachers == null) {
            return null;
        }

        return teachers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
