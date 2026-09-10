package com.campusdrive.portal.service;

import com.campusdrive.portal.dto.CollegeResponse;
import com.campusdrive.portal.dto.RegisterCollegeRequest;
import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.College;
import com.campusdrive.portal.repository.CollegeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final PasswordEncoder passwordEncoder;

    public CollegeService(CollegeRepository collegeRepository, PasswordEncoder passwordEncoder) {
        this.collegeRepository = collegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CollegeResponse register(RegisterCollegeRequest req) {
        if (collegeRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        College college = new College();
        college.setName(req.getName());
        college.setEmail(req.getEmail());
        college.setPassword(passwordEncoder.encode(req.getPassword()));
        college.setAddress(req.getAddress());
        college.setStatus(ApprovalStatus.PENDING);

        return new CollegeResponse(collegeRepository.save(college));
    }

    public List<CollegeResponse> listAll() {
        return collegeRepository.findAll().stream().map(CollegeResponse::new).toList();
    }
}