package com.example.demo.controller;

import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capacity-rules")
public class TeamCapacityRuleController {

    private final TeamCapacityRuleService service;

    public TeamCapacityRuleController(TeamCapacityRuleService service) {
        this.service = service;
    }

    @PostMapping
    public TeamCapacityConfig create(
            @RequestBody TeamCapacityConfig rule) {
        return service.createRule(rule);
    }

    @PutMapping("/{id}")
    public TeamCapacityConfig update(
            @PathVariable Long id,
            @RequestBody TeamCapacityConfig rule) {
        return service.updateRule(id, rule);
    }

    @GetMapping("/team/{teamName}")
    public TeamCapacityConfig getByTeam(
            @PathVariable String teamName) {
        return service.getRuleByTeam(teamName);
    }
}
