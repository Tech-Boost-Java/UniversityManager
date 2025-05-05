package com.softserve.academy.repository;

import com.softserve.academy.TestApplication;
import com.softserve.academy.model.Course;
import com.softserve.academy.model.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
public class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void whenFindByEmail_thenReturnTeacher() {
        // given
        Teacher teacher = new Teacher("John", "Smith", "john.smith@example.com");
        entityManager.persist(teacher);
        entityManager.flush();

        // when
        Optional<Teacher> found = teacherRepository.findByEmail(teacher.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(teacher.getName());
        assertThat(found.get().getEmail()).isEqualTo(teacher.getEmail());
    }

    @Test
    public void whenFindByNameContainingIgnoreCase_thenReturnTeachers() {
        // given
        Teacher teacher1 = new Teacher("John", "Smith", "john.smith@example.com");
        Teacher teacher2 = new Teacher("Jane", "Smith", "jane.smith@example.com");
        Teacher teacher3 = new Teacher("Bob", "Johnson", "bob.johnson@example.com");

        entityManager.persist(teacher1);
        entityManager.persist(teacher2);
        entityManager.persist(teacher3);
        entityManager.flush();

        // when
        List<Teacher> foundTeachers = teacherRepository.findByNameContainingIgnoreCase("smith");

        // then
        assertThat(foundTeachers).hasSize(2);
        assertThat(foundTeachers).extracting(Teacher::getName).containsExactlyInAnyOrder("John Smith", "Jane Smith");
    }

    @Test
    public void whenFindByCourseName_thenReturnTeachers() {
        // given
        Teacher teacher1 = new Teacher("John", "Smith", "john.smith@example.com");
        Teacher teacher2 = new Teacher("Jane", "Doe", "jane.doe@example.com");

        entityManager.persist(teacher1);
        entityManager.persist(teacher2);

        Course course1 = new Course("Java Programming", "Introduction to Java programming language");
        course1.setTeacher(teacher1);
        Course course2 = new Course("Advanced Java", "Advanced topics in Java programming");
        course2.setTeacher(teacher2);
        Course course3 = new Course("Python Programming", "Introduction to Python programming language");
        course3.setTeacher(teacher2);

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();

        // when
        List<Teacher> foundTeachers = teacherRepository.findByCourseName("java");

        // then
        assertThat(foundTeachers).hasSize(2);
        assertThat(foundTeachers).extracting(Teacher::getName).containsExactlyInAnyOrder("John Smith", "Jane Doe");
    }

    @Test
    public void whenCountCoursesByTeacherId_thenReturnCount() {
        // given
        Teacher teacher = new Teacher("John", "Smith", "john.smith@example.com");
        entityManager.persist(teacher);

        Course course1 = new Course("Java Programming", "Introduction to Java programming language");
        course1.setTeacher(teacher);
        Course course2 = new Course("Python Programming", "Introduction to Python programming language");
        course2.setTeacher(teacher);

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();

        // when
        Long courseCount = teacherRepository.countCoursesByTeacherId(teacher.getId());

        // then
        assertThat(courseCount).isEqualTo(2);
    }

    @Test
    public void whenSaveTeacher_thenFindById() {
        // given
        Teacher teacher = new Teacher("John", "Smith", "john.smith@example.com");

        // when
        Teacher savedTeacher = teacherRepository.save(teacher);
        Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());

        // then
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getName()).isEqualTo(teacher.getName());
        assertThat(foundTeacher.get().getEmail()).isEqualTo(teacher.getEmail());
    }
}
