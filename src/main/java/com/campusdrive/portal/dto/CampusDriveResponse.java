package com.campusdrive.portal.dto;

import com.campusdrive.portal.entity.CampusDrive;
import com.campusdrive.portal.entity.DriveStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CampusDriveResponse {
    private final Long id;
    private final Long companyId;
    private final String companyName;
    private final Long collegeId;
    private final String collegeName;
    private final String proposedRequirements;
    private final DriveStatus status;
    private final String declineNote;
    private final LocalDateTime requestedAt;
    private final LocalDateTime respondedAt;
    private final LocalDateTime expiresAt;
    private final Long previousDriveId;

    public CampusDriveResponse(CampusDrive drive) {
        this.id = drive.getId();
        this.companyId = drive.getCompany().getId();
        this.companyName = drive.getCompany().getName();
        this.collegeId = drive.getCollege().getId();
        this.collegeName = drive.getCollege().getName();
        this.proposedRequirements = drive.getProposedRequirements();
        this.status = drive.getStatus();
        this.declineNote = drive.getDeclineNote();
        this.requestedAt = drive.getRequestedAt();
        this.respondedAt = drive.getRespondedAt();
        this.expiresAt = drive.getExpiresAt();
        this.previousDriveId = drive.getPreviousDriveId();
    }
}