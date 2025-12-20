package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {
    private final EmployeeProfileRepository employeeRepo;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile entity = new EmployeeProfile();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setTeamName(dto.getTeamName());
        entity.setRole(dto.getRole());
        employeeRepo.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile entity = employeeRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        entity.setFullName(dto.getFullName());
        entity.setTeamName(dto.getTeamName());
        entity.setRole(dto.getRole());
        employeeRepo.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile entity = employeeRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        entity.setActive(false);
        employeeRepo.save(entity);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        EmployeeProfile entity = employeeRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        EmployeeProfileDto dto = new EmployeeProfileDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setTeamName(entity.getTeamName());
        dto.setRole(entity.getRole());
        return dto;
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return employeeRepo.findByTeamNameAndActiveTrue(teamName).stream().map(entity -> {
            EmployeeProfileDto dto = new EmployeeProfileDto();
            dto.setId(entity.getId());
            dto.setEmployeeId(entity.getEmployeeId());
            dto.setFullName(entity.getFullName());
            dto.setEmail(entity.getEmail());
            dto.setTeamName(entity.getTeamName());
            dto.setRole(entity.getRole());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return employeeRepo.findAll().stream().map(entity -> {
            EmployeeProfileDto dto = new EmployeeProfileDto();
            dto.setId(entity.getId());
            dto.setEmployeeId(entity.getEmployeeId());
            dto.setFullName(entity.getFullName());
            dto.setEmail(entity.getEmail());
            dto.setTeamName(entity.getTeamName());
            dto.setRole(entity.getRole());
            return dto;
        }).collect(Collectors.toList());
    }
}
