package com.descam.backend.service;

import com.descam.backend.entity.EmailVerificationToken;
import com.descam.backend.repository.EmailVerificationTokenRepository;
import com.descam.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepository,
            UserRepository userRepository,
            VerificationTokenService verificationTokenService) {

        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.verificationTokenService = verificationTokenService;
    }

    @Transactional
    public void verifyEmail(String rawToken) {

        String tokenHash =
                verificationTokenService.hashToken(rawToken);

        EmailVerificationToken token = tokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid verification link"
                        ));

        if (token.getUsedAt() != null) {
            throw new IllegalArgumentException(
                    "This verification link has already been used"
            );
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Verification link has expired"
            );
        }

        var user = token.getUser();

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException(
                    "Email is already verified"
            );
        }

        user.setEmailVerified(true);
        token.setUsedAt(LocalDateTime.now());
    }
}