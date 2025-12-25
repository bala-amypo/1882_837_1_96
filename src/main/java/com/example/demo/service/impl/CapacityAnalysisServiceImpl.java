package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.*;
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

    // Matches the 4-arg constructor used in LeaveOverlapTeamCapacityAnalyzerTest.setup()
    public CapacityAnalysisServiceImpl(TeamCapacityConfigRepository configRepo, 
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
        // Validation for Test Priority 68
        if (start == null || end == null || start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }
        
        // Validation for Test Priority 67
        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        // Validation for Test Priority 69
        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        List<LocalDate> days = DateRangeUtil.daysBetween(start, end);
        // This method must be defined in LeaveRequestRepository with @Query
        List<LeaveRequest> leaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);
        
        // Use TreeMap to ensure dates are sorted for Test Priority 66/70
        Map<LocalDate, Double> capacityMap = new TreeMap<>();
        boolean risky = false;

        for (LocalDate day : days) {
            // Count employees of the specific team who have approved leave on 'day'
            long onLeaveCount = leaves.stream()
                    .filter(l -> !day.isBefore(l.getStartDate()) && !day.isAfter(l.getEndDate()))
                    .count();
            
            // Percentage = ((Total - OnLeave) / Total) * 100
            double currentCapacity = ((double)(config.getTotalHeadcount() - onLeaveCount) / config.getTotalHeadcount()) * 100.0;
            capacityMap.put(day, currentCapacity);

            // Test Priority 66: If capacity falls below threshold, it's risky and alert is saved
            if (currentCapacity < config.getMinCapacityPercent()) {
                risky = true;
                CapacityAlert alert = new CapacityAlert(teamName, day, "HIGH", "Capacity dropped to " + currentCapacity + "%");
                alertRepo.save(alert);
            }
        }
        return new CapacityAnalysisResultDto(risky, capacityMap);
    }
}