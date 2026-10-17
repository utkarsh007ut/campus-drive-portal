package com.campusdrive.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private String name;

    public AuthResponse(String token,String role,Long userId,String name){
        this.token=token;
        this.role=role;
        this.userId=userId;
        this.name=name;
    }
}