package com.campusdrive.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddStudentRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password; // temporary password; student should change it after first login (future feature)

    private Double cgpa;
    private String skills;
}
