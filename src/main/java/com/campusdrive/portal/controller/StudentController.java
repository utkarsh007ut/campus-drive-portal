package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.AddStudentRequest;
import com.campusdrive.portal.dto.StudentResponse;
import com.campusdrive.portal.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@PreAuthorize("hasRole('COLLEGE')")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentResponse addStudent(@Valid @RequestBody AddStudentRequest req, Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return studentService.addStudent(collegeId, req);
    }

    @GetMapping
    public List<StudentResponse> listStudents(Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return studentService.getStudentsForCollege(collegeId);
    }
}