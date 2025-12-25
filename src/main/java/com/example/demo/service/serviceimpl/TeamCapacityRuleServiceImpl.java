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
    public TeamCapacityConfig createRule(TeamCapacityConfig rule) {
        validate(rule);
        return repo.save(rule);
    }

    @Override
    public TeamCapacityConfig updateRule(Long id, TeamCapacityConfig updatedRule) {
        TeamCapacityConfig existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
        validate(updatedRule);
        existing.setTotalHeadcount(updatedRule.getTotalHeadcount());
        existing.setMinCapacityPercent(updatedRule.getMinCapacityPercent());
        return repo.save(existing);
    }

    @Override
    public TeamCapacityConfig getRuleByTeam(String teamName) {
        return repo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Capacity config not found"));
    }

    private void validate(TeamCapacityConfig rule) {
        if (rule.getTotalHeadcount() == null || rule.getTotalHeadcount() <= 0) {
            throw new BadRequestException("Invalid total headcount");
        }
        if (rule.getMinCapacityPercent() < 1 || rule.getMinCapacityPercent() > 100) {
            throw new BadRequestException("Min capacity percent must be between 1 and 100");
        }
    }
}