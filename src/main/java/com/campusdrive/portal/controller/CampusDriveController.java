package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.CampusDriveResponse;
import com.campusdrive.portal.dto.DeclineDriveRequest;
import com.campusdrive.portal.dto.ProposeDriveRequest;
import com.campusdrive.portal.service.CampusDriveService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
public class CampusDriveController {

    private final CampusDriveService driveService;

    public CampusDriveController(CampusDriveService driveService) {
        this.driveService = driveService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public CampusDriveResponse propose(@Valid @RequestBody ProposeDriveRequest req, Authentication authentication) {
        Long companyId = (Long) authentication.getPrincipal();
        return driveService.proposeDrive(companyId, req);
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('COLLEGE')")
    public CampusDriveResponse accept(@PathVariable Long id, Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return driveService.acceptDrive(id, collegeId);
    }

    @PostMapping("/{id}/decline")
    @PreAuthorize("hasRole('COLLEGE')")
    public CampusDriveResponse decline(@PathVariable Long id, @Valid @RequestBody DeclineDriveRequest req,
                                       Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return driveService.declineDrive(id, collegeId, req);
    }


    @GetMapping("/college")
    @PreAuthorize("hasRole('COLLEGE')")
    public List<CampusDriveResponse> forCollege(Authentication authentication) {
        Long collegeId = (Long) authentication.getPrincipal();
        return driveService.getDrivesForCollege(collegeId);
    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public List<CampusDriveResponse> forCompany(Authentication authentication) {
        Long companyId = (Long) authentication.getPrincipal();
        return driveService.getDrivesForCompany(companyId);
    }
}