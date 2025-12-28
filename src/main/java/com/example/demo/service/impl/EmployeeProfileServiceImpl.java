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

    private final EmployeeProfileRepository repo;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {

        EmployeeProfile employee = new EmployeeProfile();

        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setTeamName(dto.getTeamName());
        employee.setRole(dto.getRole());
        employee.setActive(true);

        EmployeeProfile saved = repo.save(employee);
        return convertToDto(saved);
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {

        EmployeeProfile existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setTeamName(dto.getTeamName());
        existing.setRole(dto.getRole());

        return convertToDto(repo.save(existing));
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile employee = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setActive(false);
        repo.save(employee);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        EmployeeProfile employee = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        return convertToDto(employee);
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repo.findByTeamNameAndActiveTrue(teamName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EmployeeProfileDto convertToDto(EmployeeProfile e) {
        EmployeeProfileDto dto = new EmployeeProfileDto();

        dto.setId(e.getId());
        dto.setEmployeeId(e.getEmployeeId());
        dto.setFullName(e.getFullName());
        dto.setEmail(e.getEmail());
        dto.setTeamName(e.getTeamName());
        dto.setRole(e.getRole());

        return dto;
    }
}
