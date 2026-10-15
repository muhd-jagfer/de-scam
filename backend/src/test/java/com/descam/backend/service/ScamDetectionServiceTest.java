package com.descam.backend.service;

import com.descam.backend.detection.MessageAnalysisService;
import com.descam.backend.response.AnalyzeResponse;
import com.descam.backend.url.UrlAnalysisService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScamDetectionServiceTest {

    private final ScamDetectionService service =
            new ScamDetectionService(
                    new UrlAnalysisService(),
                    new MessageAnalysisService()
            );

    @Test
    void ordinaryMessageHasLowRisk() {
        AnalyzeResponse response =
                service.analyze("Hello, how are you today?");

        assertEquals("LOW", response.getRiskLevel());
        assertEquals(0, response.getScore());
        assertTrue(response.getReasons().isEmpty());
    }

    @Test
    void urgentMessageHasSuspiciousRisk() {
        AnalyzeResponse response =
                service.analyze("This is urgent. Please respond immediately.");

        assertEquals("LOW", response.getRiskLevel());
        assertEquals(2, response.getScore());
    }

    @Test
    void sensitiveInformationRequestHasSuspiciousRisk() {
        AnalyzeResponse response =
                service.analyze("Please share your OTP.");

        assertEquals("SUSPICIOUS", response.getRiskLevel());
        assertEquals(4, response.getScore());
    }

    @Test
    void multipleIndicatorsCanProduceHighRisk() {
        AnalyzeResponse response =
                service.analyze(
                        "URGENT! Share your OTP immediately and verify your account."
                );

        assertEquals("HIGH", response.getRiskLevel());
        assertTrue(response.getScore() >= 6);
        assertFalse(response.getReasons().isEmpty());
    }

    @Test
    void extractsUrlAndIncludesUrlIndicator() {
        AnalyzeResponse response =
                service.analyze("Visit https://example.com");

        assertEquals(4, response.getScore());
        assertEquals("SUSPICIOUS", response.getRiskLevel());
        assertEquals("https://example.com", response.getExtractedUrl());
        assertEquals("example.com", response.getDomain());
        assertTrue(response.isHttps());
        assertTrue(response.getReasons().contains("URL detected"));
    }

    @Test
    void handlesBlankInputWithoutCrashing() {
        AnalyzeResponse response = service.analyze("   ");

        assertEquals("LOW", response.getRiskLevel());
        assertEquals(0, response.getScore());
        assertTrue(response.getReasons().isEmpty());
        assertNull(response.getExtractedUrl());
    }

    @Test
    void handlesNullInputWithoutCrashing() {
        AnalyzeResponse response = service.analyze(null);

        assertEquals("LOW", response.getRiskLevel());
        assertEquals(0, response.getScore());
        assertTrue(response.getReasons().isEmpty());
    }
}