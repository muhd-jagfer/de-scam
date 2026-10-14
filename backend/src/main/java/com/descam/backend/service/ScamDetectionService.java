package com.descam.backend.service;

import com.descam.backend.detection.DetectionIndicator;
import com.descam.backend.detection.MessageAnalysisService;
import com.descam.backend.response.AnalyzeResponse;
import com.descam.backend.url.UrlAnalysisService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScamDetectionService
{
    private final UrlAnalysisService urlAnalysisService;
    private final MessageAnalysisService messageAnalysisService;

    public ScamDetectionService(
            UrlAnalysisService urlAnalysisService,
            MessageAnalysisService messageAnalysisService)
    {
        this.urlAnalysisService = urlAnalysisService;
        this.messageAnalysisService = messageAnalysisService;
    }

    public AnalyzeResponse analyze(String message)
    {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        String extractedUrl = urlAnalysisService.extractUrl(message);
        String domain = null;
        boolean https = false;

        // Analyze the message
        List<DetectionIndicator> Indicators = messageAnalysisService.detectIndicators(message);

        for (DetectionIndicator indicator : Indicators)
        {
            score += indicator.getScore();
            reasons.add(indicator.getDescription());
        }

        // Analyze the URL
        if (extractedUrl != null)
        {
            score += 4;
            reasons.add("URL detected");

            domain = urlAnalysisService.extractDomain(extractedUrl);
            https = urlAnalysisService.isHttps(extractedUrl);
        }

        // Determine risk level
        String riskLevel;

        if (score >= 6)
        {
            riskLevel = "HIGH";
        } else if (score >= 3) {
            riskLevel = "SUSPICIOUS";
        } else {
            riskLevel = "LOW";
        }

        return new AnalyzeResponse(
                riskLevel,
                score,
                reasons,
                extractedUrl,
                domain,
                https
        );
    }
}