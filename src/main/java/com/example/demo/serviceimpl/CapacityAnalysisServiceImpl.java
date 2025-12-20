package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.CapacityAlertRepository;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.CapacityAnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CapacityAnalysisServiceImpl implements CapacityAnalysisService {

    private final TeamCapacityConfigRepository capacityRepo;
    private final EmployeeProfileRepository employeeRepo;
    private final LeaveRequestRepository leaveRepo;
    private final CapacityAlertRepository alertRepo;

    public CapacityAnalysisServiceImpl(
            TeamCapacityConfigRepository capacityRepo,
            EmployeeProfileRepository employeeRepo,
            LeaveRequestRepository leaveRepo,
            CapacityAlertRepository alertRepo) {

        this.capacityRepo = capacityRepo;
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

        TeamCapacityConfig config = capacityRepo.findByTeamName(teamName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Capacity config not found"));

        int totalHeadcount = config.getTotalHeadcount();
        double minPercent = config.getMinCapacityPercent();

        List<LeaveRequest> leaves =
                leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);

        Map<LocalDate, Integer> leaveCountByDate = new HashMap<>();

        for (LeaveRequest lr : leaves) {
            LocalDate d = lr.getStartDate();
            while (!d.isAfter(lr.getEndDate())) {
                leaveCountByDate.put(
                        d, leaveCountByDate.getOrDefault(d, 0) + 1
                );
                d = d.plusDays(1);
            }
        }

        Map<LocalDate, Double> capacityByDate = new HashMap<>();
        boolean risky = false;

        LocalDate current = start;
        while (!current.isAfter(end)) {

            int onLeave = leaveCountByDate.getOrDefault(current, 0);
            double capacity =
                    ((double) (totalHeadcount - onLeave) / totalHeadcount) * 100;

            capacityByDate.put(current, capacity);

            if (capacity < minPercent) {
                risky = true;

                CapacityAlert alert = new CapacityAlert();
                alert.setTeamName(teamName);
                alert.setDate(current);
                alert.setSeverity("HIGH");
                alert.setMessage("Capacity below threshold");
                alertRepo.save(alert);
            }

            current = current.plusDays(1);
        }

        CapacityAnalysisResultDto dto = new CapacityAnalysisResultDto();
        dto.setRisky(risky);
        dto.setCapacityByDate(capacityByDate);
        return dto;
    }
}
