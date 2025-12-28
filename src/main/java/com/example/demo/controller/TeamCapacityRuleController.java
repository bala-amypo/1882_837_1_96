
// package com.example.demo.controller;

// import com.example.demo.model.TeamCapacityConfig;
// import com.example.demo.service.TeamCapacityRuleService;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/capacity-rules")
// public class TeamCapacityRuleController {

//     private final TeamCapacityRuleService service;

//     public TeamCapacityRuleController(TeamCapacityRuleService service) {
//         this.service = service;
//     }

//     @PostMapping
//     public TeamCapacityConfig create(@RequestBody TeamCapacityConfig config) {
//         return service.createRule(config);
//     }

//     @PutMapping("/{id}")
//     public TeamCapacityConfig update(
//             @PathVariable Long id,
//             @RequestBody TeamCapacityConfig config
//     ) {
//         return service.updateRule(id, config);
//     }

//     @GetMapping("/team/{teamName}")
//     public TeamCapacityConfig getByTeam(@PathVariable String teamName) {
//         return service.getRuleByTeam(teamName);
//     }
// }

package com.example.demo.controller;

import com.example.demo.model.TeamCapacityConfig;
import com.example.demo.service.TeamCapacityRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/capacity-rules")
@SecurityRequirement(name = "bearerAuth")
public class TeamCapacityRuleController {
    private final TeamCapacityRuleService service;

    public TeamCapacityRuleController(TeamCapacityRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TeamCapacityConfig> create(@RequestBody TeamCapacityConfig config) {
        return ResponseEntity.ok(service.createRule(config));
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<TeamCapacityConfig> getByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(service.getRuleByTeam(teamName));
    }

}