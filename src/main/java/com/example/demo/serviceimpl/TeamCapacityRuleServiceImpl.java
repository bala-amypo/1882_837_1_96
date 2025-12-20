package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.repository.TeamCapacityConfigRepository;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.stereotype.Service;

@Service
public class TeamCapacityRuleServiceImpl implements TeamCapacityRuleService {

    private final TeamCapacityConfigRepository repo;

    public TeamCapacityRuleServiceImpl(TeamCapacityConfigRepository repo) {
        this.repo = repo;
    }

    @Override
    public TeamCapacityConfig createRule(TeamCapacityConfig config) {

        if (config.getTotalHeadcount() < 1) {
            throw new BadRequestException("Invalid total headcount");
        }

        return repo.save(config);
    }

    @Override
    public TeamCapacityConfig updateRule(Long id, TeamCapacityConfig config) {

        TeamCapacityConfig existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));

        existing.setTotalHeadcount(config.getTotalHeadcount());
        existing.setMinCapacityPercent(config.getMinCapacityPercent());

        return repo.save(existing);
    }

    @Override
    public TeamCapacityConfig getRuleByTeam(String teamName) {
        return repo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));
    }
}
