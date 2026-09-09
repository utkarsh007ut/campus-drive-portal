package com.campusdrive.portal.repository;

import com.campusdrive.portal.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobId(Long jobId);
    List<Application> findByStudentId(Long studentId);
    Optional<Application> findByJobIdAndStudentId(Long jobId, Long studentId);
}