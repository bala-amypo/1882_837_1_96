package com.example.demo.controller;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeProfileController {
    private final EmployeeProfileService service;

    public EmployeeProfileController(EmployeeProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmployeeProfileDto> create(@RequestBody EmployeeProfileDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfileDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<List<EmployeeProfileDto>> getByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(service.getByTeam(teamName));
    }
}