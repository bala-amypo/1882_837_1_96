package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacityAnalysisResultDto {
    private boolean risky;
    private Map<LocalDate, Double> capacityByDate;
}