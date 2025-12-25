package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeProfileRepository empRepo;

    // Manual Constructor Injection (Matches your Test class setup)
    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRepo, EmployeeProfileRepository empRepo) {
        this.leaveRepo = leaveRepo;
        this.empRepo = empRepo;
    }

    @Override
    @Transactional
    public LeaveRequestDto create(LeaveRequestDto dto) {
        // 1. Validate Dates (Required for test case priority 17)
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new BadRequestException("Start and End dates are required");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        // 2. Find Employee (Required for test case priority 18)
        EmployeeProfile emp = empRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        
        // 3. Map DTO to Entity
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setType(dto.getType());
        leave.setStatus("PENDING"); // Default status for new requests
        leave.setReason(dto.getReason());
        
        LeaveRequest saved = leaveRepo.save(leave);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public LeaveRequestDto approve(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
        leave.setStatus("APPROVED");
        return mapToDto(leaveRepo.save(leave));
    }

    @Override
    @Transactional
    public LeaveRequestDto reject(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
        leave.setStatus("REJECTED");
        return mapToDto(leaveRepo.save(leave));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        EmployeeProfile emp = empRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return leaveRepo.findByEmployee(emp)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequestDto> getOverlappingForTeam(String teamName, LocalDate start, LocalDate end) {
        return leaveRepo.findApprovedOverlappingForTeam(teamName, start, end)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to map LeaveRequest Entity to LeaveRequestDto.
     * Aligns with the constructor expected by your DTO.
     */
    private LeaveRequestDto mapToDto(LeaveRequest l) {
        if (l == null) return null;
        
        Long employeeId = (l.getEmployee() != null) ? l.getEmployee().getId() : null;
        
        return new LeaveRequestDto(
            l.getId(), 
            employeeId, 
            l.getStartDate(), 
            l.getEndDate(), 
            l.getType(), 
            l.getStatus(), 
            l.getReason()
        );
    }
}