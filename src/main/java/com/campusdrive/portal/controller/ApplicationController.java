package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.ApplicationResponse;
import com.campusdrive.portal.dto.RejectApplicationRequest;
import com.campusdrive.portal.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/job/{jobId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ApplicationResponse apply(@PathVariable Long jobId, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return applicationService.apply(jobId, studentId);
    }

    @PostMapping("/{id}/shortlist")
    @PreAuthorize("hasRole('COLLEGE')")
    public ApplicationResponse shortlist(@PathVariable Long id, Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return applicationService.shortlist(id, collegeId);
    }

    @PostMapping("/{id}/reject-by-college")
    @PreAuthorize("hasRole('COLLEGE')")
    public ApplicationResponse rejectByCollege(@PathVariable Long id, @Valid @RequestBody RejectApplicationRequest req,
                                               Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return applicationService.rejectByCollege(id, collegeId, req);
    }

    @PostMapping("/{id}/company-decision")
    @PreAuthorize("hasRole('COMPANY')")
    public ApplicationResponse companyDecision(@PathVariable Long id, @RequestParam boolean select,
                                               Authentication authentication) {
        Long companyId = (Long) authentication.getPrincipal();
        return applicationService.companyDecision(id, companyId, select);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('COLLEGE') or hasRole('COMPANY')")
    public List<ApplicationResponse> forJob(@PathVariable Long jobId) {
        return applicationService.getApplicationsForJob(jobId);
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public List<ApplicationResponse> forStudent(Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return applicationService.getApplicationsForStudent(studentId);
    }
}
