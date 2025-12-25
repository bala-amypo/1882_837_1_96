package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CapacityAnalysisService;
import com.example.demo.util.DateRangeUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {
    private final TeamCapacityConfigRepository configRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(TeamCapacityConfigRepository configRepo, EmployeeProfileRepository employeeRepo, LeaveRequestRepository leaveRepo, CapacityAlertRepository alertRepo) {
        this.configRepo = configRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(String teamName, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) throw new BadRequestException("Start date is after end date");
        
        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() <= 0) throw new BadRequestException("Invalid total headcount");

        List<LocalDate> days = DateRangeUtil.daysBetween(start, end);
        List<LeaveRequest> leaves = leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);
        Map<LocalDate, Double> capacityMap = new HashMap<>();
        boolean risky = false;

        for (LocalDate day : days) {
            long count = leaves.stream().filter(l -> !day.isBefore(l.getStartDate()) && !day.isAfter(l.getEndDate())).count();
            double cap = ((double)(config.getTotalHeadcount() - count) / config.getTotalHeadcount()) * 100;
            capacityMap.put(day, cap);
            if (cap < config.getMinCapacityPercent()) {
                risky = true;
                alertRepo.save(new CapacityAlert(teamName, day, "HIGH", "Risk Alert"));
            }
        }
        return new CapacityAnalysisResultDto(risky, capacityMap);
    }
}