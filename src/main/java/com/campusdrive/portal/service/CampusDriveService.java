package com.campusdrive.portal.service;

import com.campusdrive.portal.dto.CampusDriveResponse;
import com.campusdrive.portal.dto.DeclineDriveRequest;
import com.campusdrive.portal.dto.ProposeDriveRequest;
import com.campusdrive.portal.entity.*;
import com.campusdrive.portal.repository.CampusDriveRepository;
import com.campusdrive.portal.repository.CollegeRepository;
import com.campusdrive.portal.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CampusDriveService {

    private final CampusDriveRepository driveRepository;
    private final CollegeRepository collegeRepository;
    private final CompanyRepository companyRepository;
    private final NotificationService notificationService;

    @Value("${app.drive.expiry-days}")
    private int expiryDays;

    @Value("${app.drive.max-resends}")
    private int maxResends;

    public CampusDriveService(CampusDriveRepository driveRepository, CollegeRepository collegeRepository,
                              CompanyRepository companyRepository, NotificationService notificationService) {
        this.driveRepository = driveRepository;
        this.collegeRepository = collegeRepository;
        this.companyRepository = companyRepository;
        this.notificationService = notificationService;
    }

    public CampusDriveResponse proposeDrive(Long companyId, ProposeDriveRequest req) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        College college = collegeRepository.findById(req.getCollegeId())
                .orElseThrow(() -> new IllegalArgumentException("College not found"));

        long priorAttempts = driveRepository.countByCompanyIdAndCollegeIdAndStatusIn(
                companyId, req.getCollegeId(), List.of(DriveStatus.DECLINED, DriveStatus.EXPIRED));
        if (priorAttempts >= maxResends) {
            throw new IllegalStateException(
                    "You have already sent the maximum of " + maxResends + " failed requests to this college");
        }

        CampusDrive drive = new CampusDrive();
        drive.setCompany(company);
        drive.setCollege(college);
        drive.setProposedRequirements(req.getProposedRequirements());
        drive.setStatus(DriveStatus.REQUESTED);
        drive.setRequestedAt(LocalDateTime.now());
        drive.setExpiresAt(LocalDateTime.now().plusDays(expiryDays));
        drive.setPreviousDriveId(req.getPreviousDriveId());

        CampusDrive saved = driveRepository.save(drive);

        notificationService.notify(college.getEmail(), "New Campus Drive Request",
                company.getName() + " has proposed a campus drive. Please review it in your dashboard.");

        return new CampusDriveResponse(saved);
    }

    public CampusDriveResponse acceptDrive(Long driveId, Long collegeId) {
        CampusDrive drive = getOwnedDrive(driveId, collegeId);
        assertStillPending(drive);

        drive.setStatus(DriveStatus.ACCEPTED);
        drive.setRespondedAt(LocalDateTime.now());
        CampusDrive saved = driveRepository.save(drive);

        notificationService.notify(drive.getCompany().getEmail(), "Campus Drive Accepted",
                drive.getCollege().getName() + " has accepted your campus drive request.");

        return new CampusDriveResponse(saved);
    }

    public CampusDriveResponse declineDrive(Long driveId, Long collegeId, DeclineDriveRequest req) {
        CampusDrive drive = getOwnedDrive(driveId, collegeId);
        assertStillPending(drive);

        drive.setStatus(DriveStatus.DECLINED);
        drive.setDeclineNote(req.getDeclineNote());
        drive.setRespondedAt(LocalDateTime.now());
        CampusDrive saved = driveRepository.save(drive);

        notificationService.notify(drive.getCompany().getEmail(), "Campus Drive Declined",
                drive.getCollege().getName() + " declined your request. Reason: " + req.getDeclineNote());

        return new CampusDriveResponse(saved);
    }

    public List<CampusDriveResponse> getDrivesForCollege(Long collegeId) {
        return driveRepository.findByCollegeId(collegeId).stream().map(CampusDriveResponse::new).toList();
    }

    public List<CampusDriveResponse> getDrivesForCompany(Long companyId) {
        return driveRepository.findByCompanyId(companyId).stream().map(CampusDriveResponse::new).toList();
    }

    private CampusDrive getOwnedDrive(Long driveId, Long collegeId) {
        CampusDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new IllegalArgumentException("Drive not found"));
        if (!drive.getCollege().getId().equals(collegeId)) {
            throw new IllegalStateException("This drive does not belong to your college");
        }
        return drive;
    }

    private void assertStillPending(CampusDrive drive) {
        if (drive.getStatus() != DriveStatus.REQUESTED) {
            throw new IllegalStateException(
                    "This drive has already been " + drive.getStatus() + " and cannot be changed");
        }
    }
}