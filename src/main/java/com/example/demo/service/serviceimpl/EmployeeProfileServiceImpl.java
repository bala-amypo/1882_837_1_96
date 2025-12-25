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

    // Manual Constructor (Fixes RequiredArgsConstructor error)
    public EmployeeProfileServiceImpl(EmployeeProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile entity = new EmployeeProfile();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setTeamName(dto.getTeamName());
        entity.setRole(dto.getRole());
        entity.setActive(true);
        EmployeeProfile saved = repo.save(entity);
        return mapToDto(saved);
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        entity.setFullName(dto.getFullName());
        entity.setTeamName(dto.getTeamName());
        entity.setRole(dto.getRole());
        return mapToDto(repo.save(entity));
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        entity.setActive(false);
        repo.save(entity);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        return repo.findById(id).map(this::mapToDto).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repo.findByTeamNameAndActiveTrue(teamName).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private EmployeeProfileDto mapToDto(EmployeeProfile e) {
        return new EmployeeProfileDto(e.getId(), e.getEmployeeId(), e.getFullName(), e.getEmail(), e.getTeamName(), e.getRole());
    }
}