package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.JobResponse;
import com.campusdrive.portal.dto.PublishJobRequest;
import com.campusdrive.portal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/drive/{driveId}")
    @PreAuthorize("hasRole('COLLEGE')")
    public JobResponse publish(@PathVariable Long driveId, @Valid @RequestBody PublishJobRequest req,
                               Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return jobService.publishJob(driveId, collegeId, req);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('COLLEGE') or hasRole('COMPANY')")
    public List<JobResponse> listAll() {
        return jobService.getAllJobs();
    }
}