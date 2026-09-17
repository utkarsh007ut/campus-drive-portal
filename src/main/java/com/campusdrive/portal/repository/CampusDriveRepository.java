package com.campusdrive.portal.repository;

import com.campusdrive.portal.entity.CampusDrive;
import com.campusdrive.portal.entity.DriveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface CampusDriveRepository extends JpaRepository<CampusDrive, Long> {
    List<CampusDrive> findByCollegeId(Long collegeId);
    List<CampusDrive> findByCompanyId(Long companyId);
    List<CampusDrive> findByCollegeIdAndStatus(Long collegeId, DriveStatus status);
    List<CampusDrive> findByStatusAndExpiresAtBefore(DriveStatus status, LocalDateTime now);
    // Only DECLINED/EXPIRED count toward the resend cap - ACCEPTED drives never should
    long countByCompanyIdAndCollegeIdAndStatusIn(Long companyId, Long collegeId, List<DriveStatus> statuses);
}