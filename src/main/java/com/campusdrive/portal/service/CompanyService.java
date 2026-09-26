package com.campusdrive.portal.service;

import com.campusdrive.portal.dto.CompanyResponse;
import com.campusdrive.portal.dto.RegisterCompanyRequest;
import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.Company;
import com.campusdrive.portal.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CompanyResponse register(RegisterCompanyRequest req) {
        if (companyRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        Company company = new Company();
        company.setName(req.getName());
        company.setEmail(req.getEmail());
        company.setPassword(passwordEncoder.encode(req.getPassword()));
        company.setWebsite(req.getWebsite());
        company.setStatus(ApprovalStatus.PENDING);

        return new CompanyResponse(companyRepository.save(company));
    }
}