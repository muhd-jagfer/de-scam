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
                    indicator -> indicator.getType().equals("urgent")
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
                    indicator -> indicator.getType().equals("otp")
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

}