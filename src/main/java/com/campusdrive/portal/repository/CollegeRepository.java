package com.campusdrive.portal.repository;

import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {

    Optional<College> findByEmail(String email);
    List<College> findByStatus(ApprovalStatus status);
}