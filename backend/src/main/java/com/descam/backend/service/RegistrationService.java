package com.descam.backend.service;

import com.descam.backend.dto.RegisterRequest;
import com.descam.backend.entity.User;
import com.descam.backend.repository.UserRepository;
import com.descam.backend.exception.DuplicateEmailException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenCreationService tokenCreationService;
    private final EmailService emailService;
    private final String frontendUrl;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            VerificationTokenCreationService tokenCreationService,
            EmailService emailService,
            @Value("${app.frontend-url}")
            String frontendUrl
            ) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenCreationService = tokenCreationService;
        this.emailService = emailService;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public void register(RegisterRequest request) {

        String normalizedEmail =
                request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateEmailException();
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName().trim(),
                normalizedEmail,
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        String token = tokenCreationService.createToken(savedUser);

        String verificationUrl =
                frontendUrl + "/verify-email?token=" + token;

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                verificationUrl
        );
    }
}