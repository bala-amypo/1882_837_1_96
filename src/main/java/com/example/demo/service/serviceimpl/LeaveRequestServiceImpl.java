package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestRepository leaveRepo;
    private final EmployeeProfileRepository empRepo;

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Start date cannot be in the future of end date");
        }
        EmployeeProfile emp = empRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setType(dto.getType());
        leave.setStatus("PENDING");
        leave.setReason(dto.getReason());
        
        return mapToDto(leaveRepo.save(leave));
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        LeaveRequest leave = leaveRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        leave.setStatus("APPROVED");
        return mapToDto(leaveRepo.save(leave));
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        LeaveRequest leave = leaveRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        leave.setStatus("REJECTED");
        return mapToDto(leaveRepo.save(leave));
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile emp = empRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return leaveRepo.findByEmployee(emp).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDto> getOverlappingForTeam(String teamName, LocalDate start, LocalDate end) {
        return leaveRepo.findApprovedOverlappingForTeam(teamName, start, end).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private LeaveRequestDto mapToDto(LeaveRequest l) {
        return new LeaveRequestDto(l.getId(), l.getEmployee().getId(), l.getStartDate(), l.getEndDate(), l.getType(), l.getStatus(), l.getReason());
    }
}