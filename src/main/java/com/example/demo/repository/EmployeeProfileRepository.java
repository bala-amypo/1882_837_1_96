package com.example.demo.repository;

import com.example.demo.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    List<EmployeeProfile> findByTeamNameAndActiveTrue(String teamName);
}