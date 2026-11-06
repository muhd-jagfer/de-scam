package com.descam.backend.controller;

import com.descam.backend.dto.RegisterRequest;
import com.descam.backend.service.RegistrationService;
import com.descam.backend.service.EmailService;
import com.descam.backend.response.EmailVerificationResponse;
import com.descam.backend.service.EmailVerificationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final EmailVerificationService emailVerificationService;

    public AuthController(
            RegistrationService registrationService,
            EmailService emailService,
            EmailVerificationService emailVerificationService) {
        this.registrationService = registrationService;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        registrationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registration successful. Please verify your email.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponse> VerifyEmail(
        @RequestParam String token
    ) {
        emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok(
            new EmailVerificationResponse("Email verified successfully")
        );
    }
}