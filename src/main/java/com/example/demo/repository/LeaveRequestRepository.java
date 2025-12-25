package com.example.demo.repository;

import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    
    // Fixes compilation error in LeaveRequestServiceImpl
    List<LeaveRequest> findByEmployee(EmployeeProfile employee);

    // Required for test priorities 23, 64, and 66
    @Query("SELECT l FROM LeaveRequest l WHERE l.status = 'APPROVED' AND l.employee.teamName = :team " +
           "AND l.startDate <= :end AND l.endDate >= :start")
    List<LeaveRequest> findApprovedOverlappingForTeam(@Param("team") String teamName, 
                                                      @Param("start") LocalDate start, 
                                                      @Param("end") LocalDate end);

    // Required for test priority 65
    @Query("SELECT l FROM LeaveRequest l WHERE l.status = 'APPROVED' AND :date BETWEEN l.startDate AND l.endDate")
    List<LeaveRequest> findApprovedOnDate(@Param("date") LocalDate date);
}