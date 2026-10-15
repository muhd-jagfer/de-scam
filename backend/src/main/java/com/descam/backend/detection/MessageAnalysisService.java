package com.descam.backend.detection;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class MessageAnalysisService {

    private static final Pattern URGENT_LANGUAGE = Pattern.compile(
            "\\b(urgent|immediately|act now|as soon as possible)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern SENSITIVE_REQUEST = Pattern.compile(
            "\\b(share|send|provide|give|reveal|disclose|enter|type|submit|forward)\\b"
                    + "(?:\\s+\\w+){0,4}\\s+"
                    + "\\b(otp|password|pin|passcode|verification code)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PROTECTIVE_ADVICE = Pattern.compile(
            "\\b(never|don't|dont|do not|should not|must not|avoid)\\b"
                    + "(?:\\s+\\w+){0,3}\\s+"
                    + "\\b(share|send|provide|give|reveal|disclose|enter|type|submit|forward)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern SUSPICIOUS_ACTION = Pattern.compile(
            "\\b(click this link|click here|verify your account)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern BANKING_THREAT = Pattern.compile(
            "\\b(account|bank account)\\b"
                    + "(?:\\s+\\w+){0,4}\\s+"
                    + "\\b(blocked|suspended|frozen|locked)\\b",
            Pattern.CASE_INSENSITIVE
    );

    public List<DetectionIndicator> detectIndicators(String message) {

        List<DetectionIndicator> indicators = new ArrayList<>();

        if (message == null || message.isBlank()) {
            return indicators;
        }

        String normalizedMessage = message.toLowerCase(Locale.ROOT);

        // 1. Detect urgency
        if (URGENT_LANGUAGE.matcher(normalizedMessage).find()) {
            indicators.add(
                    new DetectionIndicator(
                            "URGENT_LANGUAGE",
                            "Urgent language detected",
                            2
                    )
            );
        }

        // 2. Detect requests for sensitive information
        boolean protectiveAdvice =
                PROTECTIVE_ADVICE.matcher(normalizedMessage).find();

        boolean sensitiveRequest =
                SENSITIVE_REQUEST.matcher(normalizedMessage).find();

        if (sensitiveRequest && !protectiveAdvice) {
            indicators.add(
                    new DetectionIndicator(
                            "SENSITIVE_INFORMATION",
                            "Sensitive information request detected",
                            4
                    )
            );
        }

        // 3. Detect suspicious actions
        if (SUSPICIOUS_ACTION.matcher(normalizedMessage).find()) {
            indicators.add(
                    new DetectionIndicator(
                            "SUSPICIOUS_ACTION",
                            "Suspicious action request detected",
                            2
                    )
            );
        }

        // 4. Detect account threats
        if (BANKING_THREAT.matcher(normalizedMessage).find()) {
            indicators.add(
                    new DetectionIndicator(
                            "BANKING_THREAT",
                            "Account or banking threat detected",
                            3
                    )
            );
        }

        return indicators;
    }
}