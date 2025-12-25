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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository configRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        List<LocalDate> days = DateRangeUtil.daysBetween(start, end);
        List<LeaveRequest> leaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);
        
        Map<LocalDate, Double> capacityByDate = new HashMap<>();
        boolean isRisky = false;

        for (LocalDate day : days) {
            long onLeave = leaves.stream()
                    .filter(l -> !day.isBefore(l.getStartDate()) && !day.isAfter(l.getEndDate()))
                    .count();

            double currentCapacity = ((double) (config.getTotalHeadcount() - onLeave) / config.getTotalHeadcount()) * 100;
            capacityByDate.put(day, currentCapacity);

            if (currentCapacity < config.getMinCapacityPercent()) {
                isRisky = true;
                CapacityAlert alert = new CapacityAlert(null, teamName, day, "HIGH", 
                    "Capacity dropped to " + currentCapacity + "%");
                alertRepo.save(alert);
            }
        }

        return new CapacityAnalysisResultDto(isRisky, capacityByDate);
    }
}