package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.RegisterCompanyRequest;
import com.campusdrive.portal.entity.Company;
import com.campusdrive.portal.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public Company register(@Valid @RequestBody RegisterCompanyRequest req) {
        return companyService.register(req);
    }
}