package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {
    private final TeamCapacityConfigRepository configRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    /**
     * Matches the constructor used in the Test class setup.
     * Note: employeeRepo is requested in the test setup even if not used for calculations here.
     */
    public CapacityAnalysisServiceImpl(
            TeamCapacityConfigRepository configRepo, 
            EmployeeProfileRepository employeeRepo, 
            LeaveRequestRepository leaveRepo, 
            CapacityAlertRepository alertRepo) {
        this.configRepo = configRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    @Transactional
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        // 1. Validate Date Range (Requirement for test priority 68)
        if (start == null || end == null || start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date or dates are null");
        }

        // 2. Fetch Config (Requirement for test priority 67)
        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found for team: " + teamName));

        // 3. Validate Headcount (Requirement for test priority 69)
        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount configuration for team");
        }

        List<LocalDate> days = DateRangeUtil.daysBetween(start, end);
        List<LeaveRequest> leaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);
        
        Map<LocalDate, Double> capacityByDate = new TreeMap<>(); // Using TreeMap to keep dates sorted
        boolean isOverallRisky = false;

        for (LocalDate day : days) {
            // Count how many people are on leave on this specific day
            long onLeaveCount = leaves.stream()
                    .filter(l -> !day.isBefore(l.getStartDate()) && !day.isAfter(l.getEndDate()))
                    .count();

            // Calculate capacity percentage
            double currentCapacity = ((double) (config.getTotalHeadcount() - onLeaveCount) / config.getTotalHeadcount()) * 100.0;
            capacityByDate.put(day, currentCapacity);

            // 4. Threshold Check (Requirement for test priorities 66 & 70)
            if (currentCapacity < config.getMinCapacityPercent()) {
                isOverallRisky = true;
                
                // Create alert for the specific date
                CapacityAlert alert = new CapacityAlert(
                    teamName, 
                    day, 
                    "HIGH", 
                    "Low capacity alert: " + String.format("%.2f", currentCapacity) + "%"
                );
                alertRepo.save(alert);
            }
        }

        return new CapacityAnalysisResultDto(isOverallRisky, capacityByDate);
    }
}