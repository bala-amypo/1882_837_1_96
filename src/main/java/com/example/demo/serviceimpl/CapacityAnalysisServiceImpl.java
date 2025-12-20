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
import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        List<LeaveRequest> leaves =
                leaveRepo.findApprovedOverlappingForTeam(teamName, start, end);

        int onLeave = leaves.size();
        int available = config.getTotalHeadcount() - onLeave;

        boolean risky =
                ((double) available / config.getTotalHeadcount()) * 100
                        < config.getMinCapacityPercent();

        if (risky) {
            CapacityAlert alert = new CapacityAlert();
            alert.setTeamName(teamName);
            alert.setDate(start);
            alert.setSeverity("HIGH");
            alert.setMessage("Capacity below threshold");
            alertRepo.save(alert);
        }

        CapacityAnalysisResultDto dto = new CapacityAnalysisResultDto();
        dto.setTeamName(teamName);
        dto.setTotalHeadcount(config.getTotalHeadcount());
        dto.setAvailableCapacity(available);
        dto.setBelowThreshold(risky);
        dto.setAlerts(alertRepo.findByTeamNameAndDateBetween(teamName, start, end));

        return dto;
    }
}
