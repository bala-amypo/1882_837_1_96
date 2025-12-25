package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileDto {
    private Long id;
    private String employeeId;
    private String fullName;
    private String email;
    private String teamName;
    private String role;
}