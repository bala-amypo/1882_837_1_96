package com.example.demo.repository;

import com.example.demo.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(
            com.example.demo.model.EmployeeProfile employee);

    List<LeaveRequest> findApprovedOverlappingForTeam(
            String teamName, LocalDate start, LocalDate end);

    // 🔴 REQUIRED BY TESTS
    List<LeaveRequest> findApprovedOnDate(LocalDate date);
}
