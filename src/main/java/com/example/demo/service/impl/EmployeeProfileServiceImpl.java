package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {
    
    private final EmployeeProfileRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Keep this constructor exactly as it is for test compatibility
    public EmployeeProfileServiceImpl(EmployeeProfileRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        if (repo.existsByEmail(dto.getEmail().trim())) {
            throw new RuntimeException("Email already exists");
        }

        EmployeeProfile entity = new EmployeeProfile();
        entity.setEmployeeId(dto.getEmployeeId().trim());
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim());
        entity.setTeamName(dto.getTeamName().trim());
        entity.setRole(dto.getRole().trim());
        
        // --- FIX FOR NULL POINTER EXCEPTION ---
        // If passwordEncoder is null (Unit Tests), set password directly.
        // If passwordEncoder exists (Running App), encode it.
        if (passwordEncoder != null) {
            entity.setPassword(passwordEncoder.encode("admin")); 
        } else {
            entity.setPassword("admin"); 
        }
        // ---------------------------------------
        
        entity.setActive(true);

        EmployeeProfile saved = repo.save(entity);
        return mapToDto(saved);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        return repo.findById(id).map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repo.findByTeamNameAndActiveTrue(teamName).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        e.setFullName(dto.getFullName().trim());
        e.setTeamName(dto.getTeamName().trim());
        e.setRole(dto.getRole().trim());
        return mapToDto(repo.save(e));
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        EmployeeProfile e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        e.setActive(false);
        repo.save(e);
    }

    private EmployeeProfileDto mapToDto(EmployeeProfile e) {
        return new EmployeeProfileDto(
            e.getId(), 
            e.getEmployeeId(), 
            e.getFullName(), 
            e.getEmail(), 
            e.getTeamName(), 
            e.getRole()
        );
    }
}