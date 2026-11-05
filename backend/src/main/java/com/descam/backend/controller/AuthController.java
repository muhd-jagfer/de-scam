package com.descam.backend.controller;

import com.descam.backend.dto.RegisterRequest;
import com.descam.backend.service.RegistrationService;
import com.descam.backend.service.EmailService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final EmailService emailService;

    public AuthController(
            RegistrationService registrationService,
            EmailService emailService) {
        this.registrationService = registrationService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        registrationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registration successful. Please verify your email.");
    }

    @PostMapping("/test-email")
    public ResponseEntity<String> testEmail(
            @RequestParam String recipient) {

        emailService.sendTestEmail(recipient);

        return ResponseEntity.ok("Test email sent successfully.");
    }
}