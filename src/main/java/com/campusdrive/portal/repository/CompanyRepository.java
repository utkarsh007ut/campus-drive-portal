package com.campusdrive.portal.repository;

import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
    List<Company> findByStatus(ApprovalStatus status);
}