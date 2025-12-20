package com.example.demo.controller;

import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.service.TeamCapacityRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capacity-rules")
@Tag(name = "Capacity Rules")
public class TeamCapacityRuleController {

    private final TeamCapacityRuleService capacityService;

    public TeamCapacityRuleController(TeamCapacityRuleService capacityService) {
        this.capacityService = capacityService;
    }

    @PostMapping
    @Operation(summary = "Create team capacity rule")
    public TeamCapacityConfig create(@RequestBody TeamCapacityConfig config) {
        return capacityService.createRule(config);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update team capacity rule")
    public TeamCapacityConfig update(
            @PathVariable Long id,
            @RequestBody TeamCapacityConfig config
    ) {
        return capacityService.updateRule(id, config);
    }

    @GetMapping("/team/{teamName}")
    @Operation(summary = "Get capacity rule by team")
    public TeamCapacityConfig getByTeam(
            @PathVariable String teamName
    ) {
        return capacityService.getRuleByTeam(teamName);
    }
}
