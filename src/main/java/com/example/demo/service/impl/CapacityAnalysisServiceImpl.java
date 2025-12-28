package com.example.demo.service.impl;

import com.example.demo.dto.CapacityAnalysisResultDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CapacityAlert;
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
            CapacityAlertRepository alertRepo
    ) {
        this.capacityRepo = capacityRepo;
        this.employeeRepo = employeeRepo;
        this.leaveRepo = leaveRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public CapacityAnalysisResultDto analyzeTeamCapacity(
            String teamName,
            LocalDate start,
            LocalDate end
    ) {

        if (!DateRangeUtil.daysBetween(start, end)) {
            throw new BadRequestException("Start date or future");
        }

     
        TeamCapacityConfig config = capacityRepo.findByTeamName(teamName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Capacity config not found"));

 
        if (config.getTotalHeadcount() == null || config.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }

        int totalHeadcount = config.getTotalHeadcount();
        int minCapacityPercent = config.getMinCapacityPercent();

        List<?> activeEmployees =
                employeeRepo.findByTeamNameAndActiveTrue(teamName);

        Map<LocalDate, Double> capacityByDate = new HashMap<>();
        boolean risky = false;

        LocalDate current = start;

        while (!current.isAfter(end)) {

            int onLeaveCount =
                    leaveRepo.findApprovedOverlappingForTeam(
                            teamName, current, current).size();

            double availablePercent =
                    ((double) (totalHeadcount - onLeaveCount) / totalHeadcount) * 100;

            capacityByDate.put(current, availablePercent);

            if (availablePercent < minCapacityPercent) {
                risky = true;

                CapacityAlert alert = new CapacityAlert(
                        teamName,
                        current,
                        "LOW",
                        "Team capacity below threshold"
                );

                alertRepo.save(alert);
            }

            current = current.plusDays(1);
        }

       return new CapacityAnalysisResultDto(risky, capacityByDate);

    }

    static class DateRangeUtil {

        private DateRangeUtil() {
        }

        public static boolean daysBetween(LocalDate start, LocalDate end) {
            if (start == null || end == null) {
                return false;
            }
            return !start.isAfter(end);
        }
    }
}
