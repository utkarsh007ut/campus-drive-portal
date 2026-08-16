package com.campusdrive.portal.service;

import com.campusdrive.portal.config.JwtUtil;
import com.campusdrive.portal.dto.AuthResponse;
import com.campusdrive.portal.dto.LoginRequest;
import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.Role;
import com.campusdrive.portal.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final CollegeRepository collegeRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AdminRepository adminRepository, CollegeRepository collegeRepository,
                       CompanyRepository companyRepository, StudentRepository studentRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.collegeRepository = collegeRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest req) {
        var admin = adminRepository.findByEmail(req.getEmail());
        if (admin.isPresent() && passwordEncoder.matches(req.getPassword(), admin.get().getPassword())) {
            String token = jwtUtil.generateToken(admin.get().getEmail(), Role.ADMIN.name(), admin.get().getId());
            return new AuthResponse(token, Role.ADMIN.name(), admin.get().getId(), admin.get().getName());
        }

        var college = collegeRepository.findByEmail(req.getEmail());
        if (college.isPresent() && passwordEncoder.matches(req.getPassword(), college.get().getPassword())) {
            if (college.get().getStatus() != ApprovalStatus.APPROVED) {
                throw new IllegalStateException("Your college account is not yet approved by Admin");
            }
            String token = jwtUtil.generateToken(college.get().getEmail(), Role.COLLEGE.name(), college.get().getId());
            return new AuthResponse(token, Role.COLLEGE.name(), college.get().getId(), college.get().getName());
        }

        var company = companyRepository.findByEmail(req.getEmail());
        if (company.isPresent() && passwordEncoder.matches(req.getPassword(), company.get().getPassword())) {
            if (company.get().getStatus() != ApprovalStatus.APPROVED) {
                throw new IllegalStateException("Your company account is not yet approved by Admin");
            }
            String token = jwtUtil.generateToken(company.get().getEmail(), Role.COMPANY.name(), company.get().getId());
            return new AuthResponse(token, Role.COMPANY.name(), company.get().getId(), company.get().getName());
        }

        var student = studentRepository.findByEmail(req.getEmail());
        if (student.isPresent() && passwordEncoder.matches(req.getPassword(), student.get().getPassword())) {
            String token = jwtUtil.generateToken(student.get().getEmail(), Role.STUDENT.name(), student.get().getId());
            return new AuthResponse(token, Role.STUDENT.name(), student.get().getId(), student.get().getName());
        }

        throw new IllegalArgumentException("Invalid email or password");
    }
}