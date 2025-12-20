package com.example.demo.repository;

import com.example.demo.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    List<EmployeeProfile> findByTeamNameAndActiveTrue(String teamName);

    Optional<EmployeeProfile> findByEmail(String email);

    Optional<EmployeeProfile> findByEmployeeId(String employeeId);
}
