package com.campusdrive.portal.service;

import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.College;
import com.campusdrive.portal.entity.Company;
import com.campusdrive.portal.repository.CollegeRepository;
import com.campusdrive.portal.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final CollegeRepository collegeRepository;
    private final CompanyRepository companyRepository;

    public AdminService(CollegeRepository collegeRepository, CompanyRepository companyRepository) {
        this.collegeRepository = collegeRepository;
        this.companyRepository = companyRepository;
    }

    public List<College> getPendingColleges() {
        return collegeRepository.findByStatus(ApprovalStatus.PENDING);
    }

    public List<Company> getPendingCompanies() {
        return companyRepository.findByStatus(ApprovalStatus.PENDING);
    }

    public College decideCollege(Long id, boolean approve) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("College not found"));
        college.setStatus(approve ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        return collegeRepository.save(college);
    }

    public Company decideCompany(Long id, boolean approve) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        company.setStatus(approve ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        return companyRepository.save(company);
    }
}