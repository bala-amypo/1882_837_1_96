package com.example.demo.dto;

import java.time.LocalDate;
import java.util.Map;

public class CapacityAnalysisResultDto {

    public boolean risky;
    public Map<LocalDate, Double> capacityByDate;
}
