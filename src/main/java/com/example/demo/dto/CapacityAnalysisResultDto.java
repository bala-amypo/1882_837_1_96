package com.example.demo.dto;
import java.time.LocalDate;
import java.util.Map;

public class CapacityAnalysisResultDto {
    private boolean risky;
    private Map<LocalDate, Double> capacityByDate;

    public CapacityAnalysisResultDto(boolean risky, Map<LocalDate, Double> capacityByDate) {
        this.risky = risky;
        this.capacityByDate = capacityByDate;
    }
    public boolean isRisky() { return risky; }
    public Map<LocalDate, Double> getCapacityByDate() { return capacityByDate; }
}