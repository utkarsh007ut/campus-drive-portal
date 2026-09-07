package com.campusdrive.portal.repository;

import com.campusdrive.portal.entity.CampusDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampusDriveRepository extends JpaRepository<CampusDrive, Long> {
    List<CampusDrive> findByCollegeId(Long collegeId);
    List<CampusDrive> findByCompanyId(Long companyId);

    // used to enforce the resend cap: how many times has this company already
    // approached this specific college?
    long countByCompanyIdAndCollegeId(Long companyId, Long collegeId);
}