package com.campusdrive.portal.controller;

import com.campusdrive.portal.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/colleges/pending")
    public Object pendingColleges() {
        return adminService.getPendingColleges();
    }

    @GetMapping("/companies/pending")
    public Object pendingCompanies() {
        return adminService.getPendingCompanies();
    }

    @PostMapping("/colleges/{id}/decide")
    public Object decideCollege(@PathVariable Long id, @RequestParam boolean approve) {
        return adminService.decideCollege(id, approve);
    }

    @PostMapping("/companies/{id}/decide")
    public Object decideCompany(@PathVariable Long id, @RequestParam boolean approve) {
        return adminService.decideCompany(id, approve);
    }
}