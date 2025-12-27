package com.example.demo.controller;

// --- CRITICAL IMPORTS START ---
import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Required for @PreAuthorize
import org.springframework.web.bind.annotation.*;
import java.util.List;
// --- CRITICAL IMPORTS END ---

@RestController
@RequestMapping("/api/employees")
public class EmployeeProfileController {
    private final EmployeeProfileService service;

    public EmployeeProfileController(EmployeeProfileService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can create
    public ResponseEntity<EmployeeProfileDto> create(@RequestBody EmployeeProfileDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Both can view
    public ResponseEntity<EmployeeProfileDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/team/{teamName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Both can view
    public ResponseEntity<List<EmployeeProfileDto>> getByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(service.getByTeam(teamName));
    }
}