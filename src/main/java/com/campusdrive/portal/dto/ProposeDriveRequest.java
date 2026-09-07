package com.campusdrive.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProposeDriveRequest {
    @NotNull
    private Long collegeId;

    @NotBlank
    private String proposedRequirements;

    // set only when this is a resend after an earlier decline
    private Long previousDriveId;
}