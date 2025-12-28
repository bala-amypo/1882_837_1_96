package com.example.demo.repository;

import com.example.demo.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    // Required for your getByTeam service method
    List<EmployeeProfile> findByTeamNameAndActiveTrue(String teamName);
    
    // Required to prevent 500 errors on duplicate emails
    boolean existsByEmail(String email);
    
    // Required for the Login/Security logic
    Optional<EmployeeProfile> findByEmail(String email);
}