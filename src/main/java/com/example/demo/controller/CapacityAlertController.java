package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.model.CapacityAlert;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.service.CapacityAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/capacity-alerts")
@Tag(name = "Capacity Alerts")
public class CapacityAlertController {

    private final CapacityAnalysisService analysisService;
    private final CapacityAlertRepository alertRepository;

    public CapacityAlertController(
            CapacityAnalysisService analysisService,
            CapacityAlertRepository alertRepository
    ) {
        this.analysisService = analysisService;
        this.alertRepository = alertRepository;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze team capacity and generate alerts")
    public CapacityAnalysisResultDto analyze(
            @RequestParam String teamName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return analysisService.analyzeTeamCapacity(teamName, startDate, endDate);
    }

    @GetMapping("/team/{teamName}")
    @Operation(summary = "Get capacity alerts by team and date range")
    public List<CapacityAlert> getAlerts(
            @PathVariable String teamName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return alertRepository.findByTeamNameAndDateBetween(
                teamName, startDate, endDate
        );
    }
}
