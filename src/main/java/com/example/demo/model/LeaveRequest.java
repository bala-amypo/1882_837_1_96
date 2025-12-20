package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EmployeeProfile employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private String type; // ANNUAL, SICK etc.
    private String status; // PENDING, APPROVED, REJECTED
    private String reason;

    public LeaveRequest() {}
}
