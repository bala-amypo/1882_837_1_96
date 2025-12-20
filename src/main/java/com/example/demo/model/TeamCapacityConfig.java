package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class TeamCapacityConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String teamName;

    private Integer totalHeadcount;
    private Integer minCapacityPercent;

    public TeamCapacityConfig() {}
}
