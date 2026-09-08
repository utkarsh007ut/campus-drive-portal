package com.campusdrive.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PublishJobRequest {
    @NotBlank
    private String title;
    private String description;
    private String requirements;
    private Double minCgpa;
}