package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.CollegeResponse;
import com.campusdrive.portal.dto.RegisterCollegeRequest;

import com.campusdrive.portal.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {


    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping("/register")
    public CollegeResponse register(@Valid @RequestBody RegisterCollegeRequest req) {
        return collegeService.register(req);
    }

    @GetMapping("/approved")
    @PreAuthorize("hasRole('COMPANY')")
    public List<CollegeResponse> listApproved() {
        return collegeService.listApproved();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CollegeResponse> listAll() {
        return collegeService.listAll();
    }
}