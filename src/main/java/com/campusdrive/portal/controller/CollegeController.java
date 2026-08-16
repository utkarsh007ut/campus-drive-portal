package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.RegisterCollegeRequest;
import com.campusdrive.portal.entity.College;
import com.campusdrive.portal.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping("/register")
    public College register(@Valid @RequestBody RegisterCollegeRequest req) {
        return collegeService.register(req);
    }


    @GetMapping
    public java.util.List<College> listAll() {
        return collegeService.listAll();
    }
}