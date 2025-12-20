package com.example.demo.dto;

import java.time.LocalDate;

public class LeaveRequestDto {

    public Long id;
    public Long employeeId;
    public LocalDate startDate;
    public LocalDate endDate;
    public String type;
    public String status;
    public String reason;
}
