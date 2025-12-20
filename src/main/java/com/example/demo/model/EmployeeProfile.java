package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class EmployeeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String employeeId;

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;
    private String teamName;
    private String role;
    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    private Set<EmployeeProfile> colleagues = new HashSet<>();

    // Getters and setters
}
