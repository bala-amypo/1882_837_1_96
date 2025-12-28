package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.security.crypto.password.PasswordEncoder; // Add this
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {
    private final EmployeeProfileRepository repo;
    private final PasswordEncoder passwordEncoder; // Add this for login support

    public EmployeeProfileServiceImpl(EmployeeProfileRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public EmployeeProfileDto create(EmployeeProfileDto dto) {
        // 1. Check if email already exists to avoid 500 error
        if (repo.existsByEmail(dto.getEmail().trim())) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        EmployeeProfile entity = new EmployeeProfile();
        
        // 2. Use .trim() on all strings to fix the " ADMIN" space issue
        entity.setEmployeeId(dto.getEmployeeId().trim());
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim().toLowerCase());
        entity.setTeamName(dto.getTeamName().trim());
        entity.setRole(dto.getRole().trim()); // This removes the space from " ADMIN"
        
        // 3. CRITICAL: Set a password so you can actually LOGIN later
        // If the DTO doesn't have a password, we set a default one ("admin")
        entity.setPassword(passwordEncoder.encode("admin")); 
        
        entity.setActive(true);
        
        EmployeeProfile saved = repo.save(entity);
        return mapToDto(saved);
    }

    @Override
    public EmployeeProfileDto getById(Long id) {
        return repo.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeeProfileDto> getByTeam(String teamName) {
        return repo.findByTeamNameAndActiveTrue(teamName)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeProfileDto> getAll() {
        return repo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeProfileDto update(Long id, EmployeeProfileDto dto) {
        EmployeeProfile entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        entity.setFullName(dto.getFullName().trim());
        entity.setTeamName(dto.getTeamName().trim());
        entity.setRole(dto.getRole().trim());
        
        return mapToDto(repo.save(entity));
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        EmployeeProfile entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        entity.setActive(false);
        repo.save(entity);
    }

    private EmployeeProfileDto mapToDto(EmployeeProfile e) {
        // Ensure your DTO constructor matches these fields
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