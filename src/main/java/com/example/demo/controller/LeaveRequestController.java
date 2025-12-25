package com.example.demo.controller;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.service.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {
    private final LeaveRequestService service;

    public LeaveRequestController(LeaveRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LeaveRequestDto> create(@RequestBody LeaveRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDto>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }
}