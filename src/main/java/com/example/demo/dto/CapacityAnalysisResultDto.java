package com.example.demo.dto;

import com.example.demo.model.CapacityAlert;
import java.util.List;

public class CapacityAnalysisResultDto {

    private String teamName;
    private int totalHeadcount;
    private int availableCapacity;
    private boolean belowThreshold;
    private List<CapacityAlert> alerts;

    public CapacityAnalysisResultDto() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTotalHeadcount() {
        return totalHeadcount;
    }

    public void setTotalHeadcount(int totalHeadcount) {
        this.totalHeadcount = totalHeadcount;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public boolean isBelowThreshold() {
        return belowThreshold;
    }

    public void setBelowThreshold(boolean belowThreshold) {
        this.belowThreshold = belowThreshold;
    }

    public List<CapacityAlert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<CapacityAlert> alerts) {
        this.alerts = alerts;
    }
}
