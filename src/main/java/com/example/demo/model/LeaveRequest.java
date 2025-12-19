package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private EmployeeProfile employee;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    private String reason;

    // ===== Constructors =====
    public LeaveRequest() {}

    public LeaveRequest(EmployeeProfile employee, LocalDate startDate,
                        LocalDate endDate, String type, String status) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.status = status;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public EmployeeProfile getEmployee() { return employee; }
    public void setEmployee(EmployeeProfile employee) { this.employee = employee; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
