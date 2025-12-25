package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {
    private final EmployeeProfileRepository repo;

    @Override
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        EmployeeProfile entity = new EmployeeProfile();
        mapDtoToEntity(dto, entity);
        entity = repo.save(entity);
        return mapEntityToDto(entity);
    }

    @Override
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        mapDtoToEntity(dto, entity);
        return mapEntityToDto(repo.save(entity));
    }

    @Override
    public void deactivate(Long id) {
        EmployeeProfile entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        entity.setActive(false);
        repo.save(entity);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        return repo.findById(id).map(this::mapEntityToDto).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repo.findByTeamNameAndActiveTrue(teamName).stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll().stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    private void mapDtoToEntity(EmployeeProfileDto dto, EmployeeProfile entity) {
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setTeamName(dto.getTeamName());
        entity.setRole(dto.getRole());
    }

    private EmployeeProfileDto mapEntityToDto(EmployeeProfile entity) {
        return new EmployeeProfileDto(entity.getId(), entity.getEmployeeId(), entity.getFullName(), 
                                      entity.getEmail(), entity.getTeamName(), entity.getRole());
    }
}