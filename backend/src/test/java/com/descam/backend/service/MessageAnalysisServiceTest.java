package com.descam.backend.service;

import com.descam.backend.detection.DetectionIndicator;
import com.descam.backend.detection.MessageAnalysisService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageAnalysisServiceTest {

private final MessageAnalysisService service =
        new MessageAnalysisService();

@Test
void detectsUrgentLanguage() {
    List<DetectionIndicator> indicators =
            service.detectIndicators(
                    "URGENT! Please respond immediately."
            );

    assertTrue(
            indicators.stream().anyMatch(
                    indicator -> indicator.getType().equals("URGENT_LANGUAGE")
            )
    );
}

@Test
void detectsSensitiveInformationRequests() {
    List<DetectionIndicator> indicators =
            service.detectIndicators(
                    "Please share your OTP."
            );

    assertTrue(
            indicators.stream().anyMatch(
                    indicator -> indicator.getType().equals("SENSITIVE_INFORMATION")
            )
    );
}

@Test
void doesNotFlagAnOrdinaryMessage() {
    List<DetectionIndicator> indicators =
            service.detectIndicators(
                    "Hello, how are you today?"
            );

    assertTrue(indicators.isEmpty());
}

@Test
void doesNotFlagProtectiveOtpAdviceAsSensitiveRequest() {
        List<DetectionIndicator> indicators = service.detectIndicators("Never share your OTP with anyone.");

        assertFalse(indicators.stream().anyMatch(indicator -> indicator.getType().equals("SENSITIVE_INFORMATION")));
}

@Test
void doesNotFlagPinInsideUnrelatedWords() {
    List<DetectionIndicator> indicators =
            service.detectIndicators(
                    "The pinwheel is spinning in the garden."
            );

    assertFalse(
            indicators.stream().anyMatch(
                    indicator ->
                            indicator.getType().equals("SENSITIVE_INFORMATION")
            )
    );
}

@Test
void detectsSuspiciousAccountVerification() {
    List<DetectionIndicator> indicators =
            service.detectIndicators(
                    "Please verify your account immediately."
            );

    assertTrue(
            indicators.stream().anyMatch(
                    indicator ->
                            indicator.getType().equals("SUSPICIOUS_ACTION")
            )
    );
}

@Test
void doesNotFlagOrdinaryBankMentionAsThreat() {
    List<DetectionIndicator> indicators =
            service.detectIndicators(
                    "I visited the bank yesterday."
            );

    assertFalse(
            indicators.stream().anyMatch(
                    indicator ->
                            indicator.getType().equals("BANKING_THREAT")
            )
    );
}

@Test
void handlesNullAndBlankMessages() {
    assertTrue(service.detectIndicators(null).isEmpty());
    assertTrue(service.detectIndicators("   ").isEmpty());
}

}