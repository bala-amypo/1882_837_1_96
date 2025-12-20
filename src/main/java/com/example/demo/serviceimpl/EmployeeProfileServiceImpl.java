package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository repository;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile emp = new EmployeeProfile();
        emp.setFullName(dto.getFullName());
        emp.setEmail(dto.getEmail());
        emp.setEmployeeId(dto.getEmployeeId());
        emp.setTeamName(dto.getTeamName());
        emp.setRole(dto.getRole());
        emp.setActive(true);
        repository.save(emp);
        dto.setId(emp.getId());
        return dto;
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        emp.setFullName(dto.getFullName());
        emp.setEmail(dto.getEmail());
        emp.setTeamName(dto.getTeamName());
        emp.setRole(dto.getRole());
        repository.save(emp);
        return dto;
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        emp.setActive(false);
        repository.save(emp);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        EmployeeProfile emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        EmployeeProfileDto dto = new EmployeeProfileDto();
        dto.setId(emp.getId());
        dto.setFullName(emp.getFullName());
        dto.setEmail(emp.getEmail());
        dto.setEmployeeId(emp.getEmployeeId());
        dto.setTeamName(emp.getTeamName());
        dto.setRole(emp.getRole());
        return dto;
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repository.findByTeamNameAndActiveTrue(teamName)
                .stream().map(emp -> {
                    EmployeeProfileDto dto = new EmployeeProfileDto();
                    dto.setId(emp.getId());
                    dto.setFullName(emp.getFullName());
                    dto.setEmail(emp.getEmail());
                    dto.setEmployeeId(emp.getEmployeeId());
                    dto.setTeamName(emp.getTeamName());
                    dto.setRole(emp.getRole());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repository.findAll().stream().map(emp -> {
            EmployeeProfileDto dto = new EmployeeProfileDto();
            dto.setId(emp.getId());
            dto.setFullName(emp.getFullName());
            dto.setEmail(emp.getEmail());
            dto.setEmployeeId(emp.getEmployeeId());
            dto.setTeamName(emp.getTeamName());
            dto.setRole(emp.getRole());
            return dto;
        }).collect(Collectors.toList());
    }
}
