package com.example.demo.service;

import com.example.demo.dto.LeaveRequestDto;
import java.util.List;

public interface LeaveRequestService {

    LeaveRequestDto create(LeaveRequestDto dto);
    LeaveRequestDto approve(Long id);
    LeaveRequestDto reject(Long id);
    List<LeaveRequestDto> getByEmployee(Long employeeId);
}
