package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "team_capacity_config",
    uniqueConstraints = @UniqueConstraint(columnNames = "teamName")
)
public class TeamCapacityConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamName;

    @Column(nullable = false)
    private int totalHeadcount;

    @Column(nullable = false)
    private int minCapacityPercent;

    public TeamCapacityConfig() {
    }

    public Long getId() {
        return id;
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

    public int getMinCapacityPercent() {
        return minCapacityPercent;
    }

    public void setMinCapacityPercent(int minCapacityPercent) {
        this.minCapacityPercent = minCapacityPercent;
    }
}
