package com.descam.backend.service;

import com.descam.backend.entity.EmailVerificationToken;
import com.descam.backend.entity.User;
import com.descam.backend.repository.EmailVerificationTokenRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VerificationTokenCreationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final VerificationTokenService verificationTokenService;

    public VerificationTokenCreationService(
            EmailVerificationTokenRepository tokenRepository,
            VerificationTokenService verificationTokenService) {

        this.tokenRepository = tokenRepository;
        this.verificationTokenService = verificationTokenService;
    }

    @Transactional
    public String createToken(User user) {

        String rawToken = verificationTokenService.generateToken();

        String tokenHash =
                verificationTokenService.hashToken(rawToken);

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        EmailVerificationToken verificationToken =
                new EmailVerificationToken(user, tokenHash, expiresAt);

        tokenRepository.save(verificationToken);

        return rawToken;
    }
}