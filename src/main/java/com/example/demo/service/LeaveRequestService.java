package com.example.demo.service;

import com.example.demo.dto.LeaveRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService {

    LeaveRequestDto create(LeaveRequestDto dto);

    LeaveRequestDto approve(Long leaveId);

    LeaveRequestDto reject(Long leaveId);

    List<LeaveRequestDto> getByEmployee(Long employeeId);

    List<LeaveRequestDto> getOverlappingForTeam(
            String teamName,
            LocalDate startDate,
            LocalDate endDate
    );
}
