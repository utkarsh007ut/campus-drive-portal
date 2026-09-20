package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.CollegeResponse;
import com.campusdrive.portal.dto.CompanyResponse;
import com.campusdrive.portal.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/colleges/pending")
    public List<CollegeResponse> pendingColleges() {

        return adminService.getPendingColleges();
    }

    @GetMapping("/companies/pending")
    public List<CompanyResponse> pendingCompanies() {

        return adminService.getPendingCompanies();
    }

    @PostMapping("/colleges/{id}/decide")
    public CollegeResponse decideCollege(@PathVariable Long id, @RequestParam boolean approve) {
        return adminService.decideCollege(id, approve);
    }

    @PostMapping("/companies/{id}/decide")
    public CompanyResponse decideCompany(@PathVariable Long id, @RequestParam boolean approve) {
        return adminService.decideCompany(id, approve);
    }
}