package com.example.demo.repository;

import com.example.demo.model.CapacityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CapacityAlertRepository extends JpaRepository<CapacityAlert, Long> {
    List<CapacityAlert> findByTeamNameAndDateBetween(String teamName, LocalDate start, LocalDate end);
}