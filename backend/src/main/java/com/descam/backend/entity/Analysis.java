package com.descam.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String riskLevel;

    @Column(nullable = false)
    private int score;

    @ElementCollection
    @CollectionTable(
        name = "analysis_reasons",
        joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "reason")
    private List<String> reasons = new ArrayList<>();

    private String extractedUrl;

    private String domain;

    private boolean https;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Analysis() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public String getExtractedUrl() {
        return extractedUrl;
    }

    public void setExtractedUrl(String extractedUrl) {
        this.extractedUrl = extractedUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}