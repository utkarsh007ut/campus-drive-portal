package com.campusdrive.portal.controller;

import com.campusdrive.portal.dto.AuthResponse;
import com.campusdrive.portal.dto.LoginRequest;
import com.campusdrive.portal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {

        return authService.login(req);
    }
}