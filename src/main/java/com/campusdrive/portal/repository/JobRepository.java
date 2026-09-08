package com.campusdrive.portal.repository;

import com.campusdrive.portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByCampusDriveId(Long campusDriveId);
}