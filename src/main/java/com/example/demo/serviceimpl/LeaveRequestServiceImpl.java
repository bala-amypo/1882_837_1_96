package com.example.demo.service.impl;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.LeaveRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository repository;

    public LeaveRequestServiceImpl(LeaveRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public LeaveRequestDto create(LeaveRequestDto dto) {
        if (dto.startDate.isAfter(dto.endDate)) {
            throw new IllegalArgumentException("Start date after end date");
        }

        LeaveRequest leave = new LeaveRequest();
        leave.setStatus("PENDING");
        repository.save(leave);
        dto.status = "PENDING";
        return dto;
    }

    @Override
    public LeaveRequestDto approve(Long id) {
        LeaveRequest leave = repository.findById(id).orElseThrow();
        leave.setStatus("APPROVED");
        repository.save(leave);
        return new LeaveRequestDto();
    }

    @Override
    public LeaveRequestDto reject(Long id) {
        LeaveRequest leave = repository.findById(id).orElseThrow();
        leave.setStatus("REJECTED");
        repository.save(leave);
        return new LeaveRequestDto();
    }

    @Override
    public List<LeaveRequestDto> getByEmployee(Long employeeId) {
        return List.of();
    }
}
