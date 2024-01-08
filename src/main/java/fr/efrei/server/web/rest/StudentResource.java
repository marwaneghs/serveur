package fr.efrei.server.web.rest;

import fr.efrei.server.domain.Student;
import fr.efrei.server.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentResource {

    public final StudentService studentService;

    public StudentResource(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = studentService.findAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("")
    public Student create(@RequestBody Student student) {
        return studentService.save(student);
    }
}