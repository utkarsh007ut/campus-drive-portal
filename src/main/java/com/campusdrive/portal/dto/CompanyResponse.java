package com.campusdrive.portal.dto;

import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.Company;
import lombok.Getter;

@Getter
public class CompanyResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String website;
    private final ApprovalStatus status;

    public CompanyResponse(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.email = company.getEmail();
        this.website = company.getWebsite();
        this.status = company.getStatus();
    }
}