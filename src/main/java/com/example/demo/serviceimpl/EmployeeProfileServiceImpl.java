package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository repo;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile e = new EmployeeProfile();
        e.setEmployeeId(dto.getEmployeeId());
        e.setFullName(dto.getFullName());
        e.setEmail(dto.getEmail());
        e.setTeamName(dto.getTeamName());
        e.setRole(dto.getRole());
        e.setActive(true);
        repo.save(e);
        dto.setId(e.getId());
        return dto;
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        e.setFullName(dto.getFullName());
        e.setTeamName(dto.getTeamName());
        e.setRole(dto.getRole());
        repo.save(e);
        dto.setId(e.getId());
        return dto;
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        e.setActive(false);
        repo.save(e);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        EmployeeProfile e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        EmployeeProfileDto dto = new EmployeeProfileDto();
        dto.setId(e.getId());
        dto.setEmployeeId(e.getEmployeeId());
        dto.setFullName(e.getFullName());
        dto.setEmail(e.getEmail());
        dto.setTeamName(e.getTeamName());
        dto.setRole(e.getRole());
        return dto;
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repo.findByTeamNameAndActiveTrue(teamName)
                .stream()
                .map(e -> {
                    EmployeeProfileDto d = new EmployeeProfileDto();
                    d.setId(e.getId());
                    d.setEmployeeId(e.getEmployeeId());
                    d.setTeamName(e.getTeamName());
                    return d;
                }).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll()
                .stream()
                .map(e -> {
                    EmployeeProfileDto d = new EmployeeProfileDto();
                    d.setId(e.getId());
                    return d;
                }).collect(Collectors.toList());
    }
}
