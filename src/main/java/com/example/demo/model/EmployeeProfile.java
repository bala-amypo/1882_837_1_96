package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "employee_profiles",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "employeeId"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;

    private String fullName;

    private String email;

    private String teamName;

    private String role;

    private boolean active = true;

    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "employee_colleagues",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "colleague_id")
    )
    private Set<EmployeeProfile> colleagues = new HashSet<>();

    /* =========================
       Getters and Setters
       ========================= */

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<EmployeeProfile> getColleagues() {
        return colleagues;
    }

    public void setColleagues(Set<EmployeeProfile> colleagues) {
        this.colleagues = colleagues;
    }
}
