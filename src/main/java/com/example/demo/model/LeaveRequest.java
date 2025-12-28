// package com.example.demo.model;

// import jakarta.persistence.*;
// import java.time.LocalDate;

// @Entity
// public class LeaveRequest {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "employee_id", nullable = false)
//     private EmployeeProfile employee;

//     private LocalDate startDate;
//     private LocalDate endDate;

//     private String type;   // ANNUAL, SICK, etc.
//     private String status; // PENDING, APPROVED, REJECTED

//     private String reason;

//     public LeaveRequest() {}

//     public LeaveRequest(EmployeeProfile employee, LocalDate startDate,
//                         LocalDate endDate, String type, String status, String reason) {
//         this.employee = employee;
//         this.startDate = startDate;
//         this.endDate = endDate;
//         this.type = type;
//         this.status = status;
//         this.reason = reason;
//     }

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public EmployeeProfile getEmployee() {
//         return employee;
//     }

//     public void setEmployee(EmployeeProfile employee) {
//         this.employee = employee;
//     }

//     public LocalDate getStartDate() {
//         return startDate;
//     }

//     public void setStartDate(LocalDate startDate) {
//         this.startDate = startDate;
//     }

//     public LocalDate getEndDate() {
//         return endDate;
//     }

//     public void setEndDate(LocalDate endDate) {
//         this.endDate = endDate;
//     }

//     public String getType() {
//         return type;
//     }

//     public void setType(String type) {
//         this.type = type;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public void setStatus(String status) {
//         this.status = status;
//     }

//     public String getReason() {
//         return reason;
//     }

//     public void setReason(String reason) {
//         this.reason = reason;
//     }

   
// }


package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeProfile employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String status;
    private String reason;

    public LeaveRequest() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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