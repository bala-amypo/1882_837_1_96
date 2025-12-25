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
        // Requirement for Test 68: Message must contain "Start date"
        if (start == null || end == null || start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }
        
        // Requirement for Test 67: Message must contain "Capacity config not found"
        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found for team: " + teamName));

        // Requirement for Test 69: Message must contain "Invalid total headcount"
        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount for this team");
        }

        List<LocalDate> days = DateRangeUtil.daysBetween(start, end);
        List<LeaveRequest> leaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);
        
        // Use TreeMap to keep dates in order (cleaner result)
        Map<LocalDate, Double> capacityMap = new TreeMap<>();
        boolean risky = false;

        for (LocalDate day : days) {
            long count = leaves.stream()
                    .filter(l -> !day.isBefore(l.getStartDate()) && !day.isAfter(l.getEndDate()))
                    .count();
            
            double cap = ((double)(config.getTotalHeadcount() - count) / config.getTotalHeadcount()) * 100.0;
            capacityMap.put(day, cap);

            // Requirement for Test 66: Check against MinCapacityPercent threshold
            if (cap < config.getMinCapacityPercent()) {
                risky = true;
                alertRepo.save(new CapacityAlert(teamName, day, "HIGH", "Low capacity: " + String.format("%.2f", cap) + "%"));
            }
        }
        return new CapacityAnalysisResultDto(risky, capacityMap);
    }
}