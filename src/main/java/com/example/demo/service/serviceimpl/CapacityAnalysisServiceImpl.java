package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.*;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository configRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    // Manual Constructor Injection (Replaces @RequiredArgsConstructor)
    public CapacityAnalysisServiceImpl(TeamCapacityConfigRepository configRepo, 
                                       EmployeeProfileRepository employeeRepo, 
                                       LeaveRequestRepository leaveRepo, 
                                       CapacityAlertRepository alertRepo) {
        this.configRepo = configRepo;
        this.employeeRepo = employeeRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new BadRequestException("Invalid date range: Start date or future");
        }

        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() == null || config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        List<LocalDate> days = DateRangeUtil.daysBetween(start, end);
        List<LeaveRequest> approvedLeaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);
        
        Map<LocalDate, Double> capacityMap = new HashMap<>();
        boolean risky = false;

        for (LocalDate day : days) {
            long countOnLeave = approvedLeaves.stream()
                .filter(l -> !day.isBefore(l.getStartDate()) && !day.isAfter(l.getEndDate()))
                .count();

            double capacity = ((double) (config.getTotalHeadcount() - countOnLeave) / config.getTotalHeadcount()) * 100.0;
            capacityMap.put(day, capacity);

            if (capacity < config.getMinCapacityPercent()) {
                risky = true;
                CapacityAlert alert = new CapacityAlert();
                alert.setTeamName(teamName);
                alert.setDate(day);
                alert.setSeverity("HIGH");
                alert.setMessage("Capacity low: " + capacity + "%");
                alertRepo.save(alert);
            }
        }
        return new CapacityAnalysisResultDto(risky, capacityMap);
    }
}