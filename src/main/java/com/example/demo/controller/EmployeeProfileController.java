package com.example.demo.controller;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.service.EmployeeProfileService;
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
    public EmployeeProfileDto create(@RequestBody EmployeeProfileDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public EmployeeProfileDto update(@PathVariable Long id, @RequestBody EmployeeProfileDto dto) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public EmployeeProfileDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/team/{teamName}")
    public List<EmployeeProfileDto> getByTeam(@PathVariable String teamName) {
        return service.getByTeam(teamName);
    }

    @GetMapping
    public List<EmployeeProfileDto> getAll() {
        return service.getAll();
    }
}
