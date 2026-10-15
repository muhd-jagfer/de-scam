package com.descam.backend.service;

import com.descam.backend.detection.DetectionIndicator;
import com.descam.backend.detection.MessageAnalysisService;
import com.descam.backend.response.AnalyzeResponse;
import com.descam.backend.url.UrlAnalysisService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScamDetectionService {

    private static final int URL_DETECTED_SCORE = 4;

    private static final int HIGH_RISK_THRESHOLD = 6;
    private static final int SUSPICIOUS_RISK_THRESHOLD = 3;

    private final UrlAnalysisService urlAnalysisService;
    private final MessageAnalysisService messageAnalysisService;

    public ScamDetectionService(
            UrlAnalysisService urlAnalysisService,
            MessageAnalysisService messageAnalysisService) {

        this.urlAnalysisService = urlAnalysisService;
        this.messageAnalysisService = messageAnalysisService;
    }

    public AnalyzeResponse analyze(String message) {

        List<String> reasons = new ArrayList<>();

        if (message == null || message.isBlank()) {
            return new AnalyzeResponse(
                    "LOW",
                    0,
                    reasons,
                    null,
                    null,
                    false
            );
        }

        int score = 0;

        String extractedUrl = urlAnalysisService.extractUrl(message);
        String domain = null;
        boolean https = false;

        // Analyze the message.
        List<DetectionIndicator> indicators =
                messageAnalysisService.detectIndicators(message);

        // Keep reasons unique while preserving their order.
        Set<String> uniqueReasons = new LinkedHashSet<>();

        for (DetectionIndicator indicator : indicators) {
            score += indicator.getScore();
            uniqueReasons.add(indicator.getDescription());
        }

        // Analyze the URL.
        if (extractedUrl != null) {
            score += URL_DETECTED_SCORE;
            uniqueReasons.add("URL detected");

            domain = urlAnalysisService.extractDomain(extractedUrl);
            https = urlAnalysisService.isHttps(extractedUrl);
        }

        reasons.addAll(uniqueReasons);

        // Determine the risk level.
        String riskLevel = determineRiskLevel(score);

        return new AnalyzeResponse(
                riskLevel,
                score,
                reasons,
                extractedUrl,
                domain,
                https
        );
    }

    private String determineRiskLevel(int score) {

        if (score >= HIGH_RISK_THRESHOLD) {
            return "HIGH";
        }

        if (score >= SUSPICIOUS_RISK_THRESHOLD) {
            return "SUSPICIOUS";
        }

        return "LOW";
    }
}