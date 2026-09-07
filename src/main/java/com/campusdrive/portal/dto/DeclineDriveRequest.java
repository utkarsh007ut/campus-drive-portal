package com.campusdrive.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeclineDriveRequest {
    @NotBlank(message = "A reason note is required when declining a drive")
    private String declineNote;
}