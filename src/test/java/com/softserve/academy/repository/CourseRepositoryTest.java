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
public class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void whenFindByName_thenReturnCourse() {
        // given
        Course course = new Course("Java Programming", "Introduction to Java programming language");
        entityManager.persist(course);
        entityManager.flush();

        // when
        Optional<Course> found = courseRepository.findByName(course.getName());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(course.getName());
        assertThat(found.get().getDescription()).isEqualTo(course.getDescription());
    }

    @Test
    public void whenFindByTeacher_thenReturnCourses() {
        // given
        Teacher teacher = new Teacher("John", "Smith", "john.smith@example.com");
        entityManager.persist(teacher);

        Course course1 = new Course("Java Programming", "Introduction to Java programming language");
        course1.setTeacher(teacher);
        Course course2 = new Course("Python Programming", "Introduction to Python programming language");
        course2.setTeacher(teacher);
        Course course3 = new Course("C# Programming", "Introduction to C# programming language");

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();

        // when
        List<Course> foundCourses = courseRepository.findByTeacher(teacher);

        // then
        assertThat(foundCourses).hasSize(2);
        assertThat(foundCourses).extracting(Course::getName).containsExactlyInAnyOrder("Java Programming", "Python Programming");
    }

    @Test
    public void whenFindByTeacherId_thenReturnCourses() {
        // given
        Teacher teacher = new Teacher("John", "Smith", "john.smith@example.com");
        entityManager.persist(teacher);

        Course course1 = new Course("Java Programming", "Introduction to Java programming language");
        course1.setTeacher(teacher);
        Course course2 = new Course("Python Programming", "Introduction to Python programming language");
        course2.setTeacher(teacher);
        Course course3 = new Course("C# Programming", "Introduction to C# programming language");

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();

        // when
        List<Course> foundCourses = courseRepository.findByTeacherId(teacher.getId());

        // then
        assertThat(foundCourses).hasSize(2);
        assertThat(foundCourses).extracting(Course::getName).containsExactlyInAnyOrder("Java Programming", "Python Programming");
    }

    @Test
    public void whenSearchCourses_thenReturnMatchingCourses() {
        // given
        Course course1 = new Course("Java Programming", "Introduction to Java programming language");
        Course course2 = new Course("Advanced Java", "Advanced topics in Java programming");
        Course course3 = new Course("Python Programming", "Introduction to Python programming language");

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();

        // when
        List<Course> foundCourses = courseRepository.searchCourses("java");

        // then
        assertThat(foundCourses).hasSize(2);
        assertThat(foundCourses).extracting(Course::getName).containsExactlyInAnyOrder("Java Programming", "Advanced Java");
    }

    @Test
    public void whenSaveCourse_thenFindById() {
        // given
        Course course = new Course("Java Programming", "Introduction to Java programming language");

        // when
        Course savedCourse = courseRepository.save(course);
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());

        // then
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getName()).isEqualTo(course.getName());
        assertThat(foundCourse.get().getDescription()).isEqualTo(course.getDescription());
    }
}
