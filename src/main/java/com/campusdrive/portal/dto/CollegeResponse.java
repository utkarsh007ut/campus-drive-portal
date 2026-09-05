package com.campusdrive.portal.dto;

import com.campusdrive.portal.entity.ApprovalStatus;
import com.campusdrive.portal.entity.College;
import lombok.Getter;

@Getter
public class CollegeResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String address;
    private final ApprovalStatus status;

    public CollegeResponse(College college) {
        this.id = college.getId();
        this.name = college.getName();
        this.email = college.getEmail();
        this.address = college.getAddress();
        this.status = college.getStatus();
    }
}