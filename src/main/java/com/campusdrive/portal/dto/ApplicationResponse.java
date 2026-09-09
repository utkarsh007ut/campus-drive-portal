package com.campusdrive.portal.dto;

import com.campusdrive.portal.entity.Application;
import com.campusdrive.portal.entity.ApplicationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplicationResponse {
    private final Long id;
    private final Long jobId;
    private final String jobTitle;
    private final Long studentId;
    private final String studentName;
    private final ApplicationStatus status;
    private final String rejectionReason;
    private final LocalDateTime appliedAt;
    private final LocalDateTime decidedAt;

    public ApplicationResponse(Application app) {
        this.id = app.getId();
        this.jobId = app.getJob().getId();
        this.jobTitle = app.getJob().getTitle();
        this.studentId = app.getStudent().getId();
        this.studentName = app.getStudent().getName();
        this.status = app.getStatus();
        this.rejectionReason = app.getRejectionReason();
        this.appliedAt = app.getAppliedAt();
        this.decidedAt = app.getDecidedAt();
    }
}