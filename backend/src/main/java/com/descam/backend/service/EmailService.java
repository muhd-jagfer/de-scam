package com.descam.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTestEmail(String recipient) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject("Descam Email Test");
        message.setText("Hello! Gmail SMTP is working with Descam.");

        mailSender.send(message);
    }
}