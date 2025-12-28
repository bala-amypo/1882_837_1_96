
// package com.example.demo.controller;

// import com.example.demo.dto.CapacityAnalysisResultDto;
// import com.example.demo.model.CapacityAlert;
// import com.example.demo.repository.CapacityAlertRepository;
// import com.example.demo.service.CapacityAnalysisService;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/capacity-alerts")
// public class CapacityAlertController {

//     private final CapacityAnalysisService analysisService;
//     private final CapacityAlertRepository alertRepository;

//     public CapacityAlertController(
//             CapacityAnalysisService analysisService,
//             CapacityAlertRepository alertRepository
//     ) {
//         this.analysisService = analysisService;
//         this.alertRepository = alertRepository;
//     }

//     @PostMapping("/analyze")
//     public CapacityAnalysisResultDto analyze(@RequestBody Map<String, String> body) {

//         String teamName = body.get("teamName");
//         LocalDate start = LocalDate.parse(body.get("start"));
//         LocalDate end = LocalDate.parse(body.get("end"));

//         return analysisService.analyzeTeamCapacity(teamName, start, end);
//     }

//     @GetMapping("/team/{teamName}")
//     public List<CapacityAlert> getAlertsByTeamAndDate(
//             @PathVariable String teamName,
//             @RequestParam LocalDate start,
//             @RequestParam LocalDate end
//     ) {
//         return alertRepository.findByTeamNameAndDateBetween(teamName, start, end);
//     }
// }

package com.example.demo.controller;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.model.CapacityAlert;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/capacity-alerts")
@SecurityRequirement(name = "bearerAuth")
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