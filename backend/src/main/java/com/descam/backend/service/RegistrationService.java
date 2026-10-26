package com.descam.backend.service;

import com.descam.backend.dto.RegisterRequest;
import com.descam.backend.entity.User;
import com.descam.backend.exception.DuplicateEmailException;
import com.descam.backend.repository.UserRepository;
import com.descam.backend.exception.DuplicateEmailException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequest request) {
        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

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

        userRepository.save(user);
    }
}