package com.descam.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String senderEmail) {

        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendVerificationEmail(
            String recipient,
            String verificationUrl) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(recipient);
        message.setSubject("Verify your Descam account");

        message.setText(
                "Welcome to Descam!\n\n"
                + "Thank you for registering.\n"
                + "Please click the link below to verify your email address:\n\n"
                + verificationUrl + "\n\n"
                + "This link expires in 24 hours.\n"
                + "If you did not create this account, you can ignore this email."
        );

        mailSender.send(message);
    }
}