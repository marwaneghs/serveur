package fr.efrei.server.web.rest;

import fr.efrei.server.domain.Student;
import fr.efrei.server.repository.StudentRepository;
import fr.efrei.server.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class StudentResourceIT {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Test
    @Transactional
    void findAllReturnsAllStudentsInTheRepository() {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        Student student = new Student();
        student.setName("Pierre");
        studentRepository.save(student);

        List<Student> studentList = studentService.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    void saveStudentIncrementsTheStudentsSizeOf1() {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(0);

        Student student = new Student();
        student.setName("Pierre");
        studentService.save(student);

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    void findByIdReturnsTheRelatedStudentData() {
        Student student = new Student();
        student.setName("Pierre");
        studentRepository.save(student);

        Student existingStudent = studentService.findById(student.getId());
        assertThat(existingStudent).isNotNull();
    }

    @Test
    @Transactional
    void findByIdThrowsExceptionWhenStudentIsNotFound() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> studentService.findById(999));
    }

    @Test
    @Transactional
    void updateExistingStudent() {
        Student student = new Student();
        student.setName("Pierre");
        studentRepository.save(student);

        student.setName("Paul");
        Student existingStudent = studentService.update(student.getId(), student);
        assertThat(existingStudent).isEqualTo(student);
    }

    @Test
    @Transactional
    void updateThrowsAnExceptionWhenStudentIdIsNotFound() {
        Student student = new Student();
        student.setName("Pierre");

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> studentService.update(999, student));
    }

    @Test
    @Transactional
    void deleteByIdStudentSucceeds() {
        Student student = new Student();
        student.setName("Pierre");
        studentRepository.save(student);

        studentService.deleteById(student.getId());
        assertThat(studentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void deleteByIdThrowsAnExceptionWhenStudentIdIsNotFound() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> studentService.deleteById(999));
    }
}
