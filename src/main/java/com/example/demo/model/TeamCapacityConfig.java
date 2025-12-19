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

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private Integer totalHeadcount;

    @Column(nullable = false)
    private Integer minCapacityPercent;

    // ===== Constructors =====
    public TeamCapacityConfig() {}

    public TeamCapacityConfig(String teamName,
                              Integer totalHeadcount,
                              Integer minCapacityPercent) {
        this.teamName = teamName;
        this.totalHeadcount = totalHeadcount;
        this.minCapacityPercent = minCapacityPercent;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Integer getTotalHeadcount() { return totalHeadcount; }
    public void setTotalHeadcount(Integer totalHeadcount) {
        this.totalHeadcount = totalHeadcount;
    }

    public Integer getMinCapacityPercent() { return minCapacityPercent; }
    public void setMinCapacityPercent(Integer minCapacityPercent) {
        this.minCapacityPercent = minCapacityPercent;
    }
}
