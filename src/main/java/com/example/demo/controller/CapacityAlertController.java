package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.model.CapacityAlert;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/capacity-alerts")
public class CapacityAlertController {
    private final CapacityAnalysisService analysisService;
    private final CapacityAlertRepository alertRepo;

    public CapacityAlertController(CapacityAnalysisService analysisService, CapacityAlertRepository alertRepo) {
        this.analysisService = analysisService;
        this.alertRepo = alertRepo;
    }

    @PostMapping("/analyze")
    public CapacityAnalysisResultDto analyze(
            @RequestParam String teamName, 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return analysisService.analyzeTeamCapacity(teamName, start, end);
    }

    @GetMapping("/team/{teamName}")
    public List<CapacityAlert> getAlerts(
            @PathVariable String teamName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return alertRepo.findByTeamNameAndDateBetween(teamName, start, end);
    }
}