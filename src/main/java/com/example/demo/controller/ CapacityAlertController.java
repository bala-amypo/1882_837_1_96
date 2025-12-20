package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/capacity")
public class CapacityAnalysisController {

    private final CapacityAnalysisService service;

    public CapacityAnalysisController(
            CapacityAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/analyze")
    public CapacityAnalysisResultDto analyze(
            @RequestParam String teamName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return service.analyzeTeamCapacity(
                teamName, startDate, endDate);
    }
}
