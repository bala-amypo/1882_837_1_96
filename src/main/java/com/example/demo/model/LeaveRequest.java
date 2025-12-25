package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeProfile employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private String type; // ANNUAL, SICK
    private String status; // PENDING, APPROVED, REJECTED
    private String reason;
}