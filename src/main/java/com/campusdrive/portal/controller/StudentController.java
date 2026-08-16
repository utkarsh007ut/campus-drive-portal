package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.AddStudentRequest;
import com.campusdrive.portal.entity.Student;
import com.campusdrive.portal.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // TEMPORARY: collegeId as a request param. Once Spring Security + JWT is
    // wired in, this will come from the logged-in college's token instead -
    // not from a value the caller can just type in.
    @PostMapping
    public Student addStudent(@RequestParam Long collegeId, @Valid @RequestBody AddStudentRequest req) {
        return studentService.addStudent(collegeId, req);
    }

    @GetMapping
    public List<Student> listStudents(@RequestParam Long collegeId) {
        return studentService.getStudentsForCollege(collegeId);
    }
}