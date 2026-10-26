package com.descam.backend.controller;

import com.descam.backend.dto.RegisterRequest;
import com.descam.backend.service.RegistrationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        registrationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registration successful. Please verify your email.");
    }
}