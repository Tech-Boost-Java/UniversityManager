package com.softserve.academy.repository;

import com.softserve.academy.TestApplication;
import com.softserve.academy.model.Student;
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
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void whenFindByEmail_thenReturnStudent() {
        // given
        Student student = new Student("John", "Doe", "john.doe@example.com");
        entityManager.persist(student);
        entityManager.flush();

        // when
        Optional<Student> found = studentRepository.findByEmail(student.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo(student.getFirstName());
        assertThat(found.get().getLastName()).isEqualTo(student.getLastName());
    }

    @Test
    public void whenFindByLastName_thenReturnStudents() {
        // given
        Student student1 = new Student("John", "Doe", "john.doe@example.com");
        Student student2 = new Student("Jane", "Doe", "jane.doe@example.com");
        Student student3 = new Student("Jim", "Smith", "jim.smith@example.com");

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.persist(student3);
        entityManager.flush();

        // when
        List<Student> foundStudents = studentRepository.findByLastName("Doe");

        // then
        assertThat(foundStudents).hasSize(2);
        assertThat(foundStudents).extracting(Student::getLastName).containsOnly("Doe");
    }

    @Test
    public void whenFindByFirstNameAndLastName_thenReturnStudents() {
        // given
        Student student1 = new Student("John", "Doe", "john.doe@example.com");
        Student student2 = new Student("John", "Smith", "john.smith@example.com");
        Student student3 = new Student("Jane", "Doe", "jane.doe@example.com");

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.persist(student3);
        entityManager.flush();

        // when
        List<Student> foundStudents = studentRepository.findByFirstNameAndLastName("John", "Doe");

        // then
        assertThat(foundStudents).hasSize(1);
        assertThat(foundStudents.get(0).getFirstName()).isEqualTo("John");
        assertThat(foundStudents.get(0).getLastName()).isEqualTo("Doe");
    }

    @Test
    public void whenSaveStudent_thenFindById() {
        // given
        Student student = new Student("John", "Doe", "john.doe@example.com");

        // when
        Student savedStudent = studentRepository.save(student);
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        // then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getFirstName()).isEqualTo(student.getFirstName());
        assertThat(foundStudent.get().getLastName()).isEqualTo(student.getLastName());
        assertThat(foundStudent.get().getEmail()).isEqualTo(student.getEmail());
    }
}
