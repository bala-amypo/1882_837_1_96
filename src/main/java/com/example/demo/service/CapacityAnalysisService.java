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

import java.time.LocalDate;
import java.util.*;

public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository configRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(
            TeamCapacityConfigRepository configRepo,
            EmployeeProfileRepository employeeRepo,
            LeaveRequestRepository leaveRepo,
            CapacityAlertRepository alertRepo) {
        this.configRepo = configRepo;
        this.employeeRepo = employeeRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(
            String teamName, LocalDate start, LocalDate end) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date is invalid");
        }

        TeamCapacityConfig config = configRepo.findByTeamName(teamName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Capacity config not found"));

        if (config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        Map<LocalDate, Double> capacityMap = new HashMap<>();
        boolean risky = false;

        for (LocalDate date : DateRangeUtil.daysBetween(start, end)) {
            List<LeaveRequest> leaves = leaveRepo.findApprovedOnDate(date);
            long onLeave = leaves.stream()
                    .filter(l -> l.getEmployee().getTeamName().equals(teamName))
                    .count();

            double capacity =
                    ((config.getTotalHeadcount() - onLeave) * 100.0)
                            / config.getTotalHeadcount();

            capacityMap.put(date, capacity);

            if (capacity < config.getMinCapacityPercent()) {
                risky = true;
                alertRepo.save(new CapacityAlert(
                        null,
                        teamName,
                        date,
                        "HIGH",
                        "Capacity below threshold"
                ));
            }
        }

        CapacityAnalysisResultDto dto = new CapacityAnalysisResultDto();
        dto.setRisky(risky);
        dto.setCapacityByDate(capacityMap);
        return dto;
    }
}
