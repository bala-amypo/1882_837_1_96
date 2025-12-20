package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;
    private String password;
    private String role;

    @OneToOne
    private EmployeeProfile employeeProfile;

    public UserAccount() {}
}
