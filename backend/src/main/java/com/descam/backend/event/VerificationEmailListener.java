package com.descam.backend.event;

import com.descam.backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class VerificationEmailListener {

    private static final Logger logger =
            LoggerFactory.getLogger(VerificationEmailListener.class);

    private final EmailService emailService;

    public VerificationEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendVerificationEmail(VerificationEmailEvent event) {
        try {
            emailService.sendVerificationEmail(
                    event.recipient(),
                    event.verificationUrl()
            );
        } catch (MailException ex) {
            logger.error(
                    "Failed to send verification email to {}",
                    event.recipient(),
                    ex
            );
        }
    }
}