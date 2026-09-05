package com.campusdrive.portal.service;

import com.campusdrive.portal.dto.AddStudentRequest;
import com.campusdrive.portal.dto.StudentResponse;
import com.campusdrive.portal.entity.College;
import com.campusdrive.portal.entity.Student;
import com.campusdrive.portal.repository.CollegeRepository;
import com.campusdrive.portal.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CollegeRepository collegeRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, CollegeRepository collegeRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.collegeRepository = collegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentResponse addStudent(Long collegeId, AddStudentRequest req) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new IllegalArgumentException("College not found"));

        if (studentRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A student with this email already exists");
        }

        Student student = new Student();
        student.setName(req.getName());
        student.setEmail(req.getEmail());
        student.setPassword(passwordEncoder.encode(req.getPassword()));
        student.setCgpa(req.getCgpa());
        student.setSkills(req.getSkills());
        student.setCollege(college);

        Student saved = studentRepository.save(student);
        return new StudentResponse(saved);
    }

    public List<StudentResponse> getStudentsForCollege(Long collegeId) {
        return studentRepository.findByCollegeId(collegeId)
                .stream()
                .map(StudentResponse::new)
                .toList();
    }
}