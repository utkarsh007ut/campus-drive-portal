package com.campusdrive.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectApplicationRequest {
    @NotBlank(message = "A rejection reason is required")
    private String reason;
}