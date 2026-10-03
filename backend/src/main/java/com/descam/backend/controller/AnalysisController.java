package com.descam.backend.controller;

import com.descam.backend.dto.AnalyzeRequest;
import com.descam.backend.response.AnalyzeResponse;
import com.descam.backend.service.ScamDetectionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalysisController
{
    private final ScamDetectionService scamDetectionService;

    public AnalysisController(ScamDetectionService scamDetectionService)
    {
        this.scamDetectionService = scamDetectionService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Welcome to Descam!";
    }

    @PostMapping("/analyze")
    public AnalyzeResponse analyze(
            @Valid @RequestBody AnalyzeRequest request)
    {
        return scamDetectionService.analyze(request.getMessage());
    }
}