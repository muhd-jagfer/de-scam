package com.descam.backend.url;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlAnalysisServiceTest {

    private final UrlAnalysisService service = new UrlAnalysisService();

    @Test
    void extractsUrlFromMessage() {
        String url = service.extractUrl(
                "Check this website: https://example.com"
        );

        assertEquals("https://example.com", url);
    }

    @Test
    void removesTrailingPunctuation() {
        String url = service.extractUrl(
                "Visit https://example.com."
        );

        assertEquals("https://example.com", url);
    }

    @Test
    void returnsNullWhenNoUrlExists() {
        String url = service.extractUrl(
                "Hello, how are you?"
        );

        assertNull(url);
    }

    @Test
    void returnsNullForEmptyMessage() {
        assertNull(service.extractUrl(""));
    }

    @Test
    void extractDomainFromUrl() {
      String domain = service.extractDomain("https://example.com/path");
      assertEquals("example.com", domain);
    }

    @Test void returnsNullWhenUrlIsInvalid() {
      String domain = service.extractDomain("not-a-valid-url");
      assertNull(domain);
    }

    @Test
    void detectHttpsUrl() {
      assertTrue(service.isHttps("https://example.com"));
    }

    @Test
    void detectsHttpUrlAsNotHttps() {
      assertFalse(service.isHttps("http://example.com"));
    }

    @Test
    void detectsInvalidUrlAsNotHttps() {
      assertFalse(service.isHttps("not-a-valid-url"));
    }
}