package com.campusdrive.portal.dto;

import com.campusdrive.portal.entity.Job;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JobResponse {
    private final Long id;
    private final Long campusDriveId;
    private final String companyName;
    private final String collegeName;
    private final String title;
    private final String description;
    private final String requirements;
    private final Double minCgpa;
    private final LocalDateTime publishedAt;

    public JobResponse(Job job) {
        this.id = job.getId();
        this.campusDriveId = job.getCampusDrive().getId();
        this.companyName = job.getCampusDrive().getCompany().getName();
        this.collegeName = job.getCampusDrive().getCollege().getName();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.minCgpa = job.getMinCgpa();
        this.publishedAt = job.getPublishedAt();
    }
}