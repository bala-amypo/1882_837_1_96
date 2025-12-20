package com.example.demo.controller;

import com.example.demo.dto.LeaveRequestDto;
import com.example.demo.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@Tag(name = "Leave Requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveService;

    public LeaveRequestController(LeaveRequestService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    @Operation(summary = "Submit a leave request")
    public LeaveRequestDto create(@RequestBody LeaveRequestDto dto) {
        return leaveService.create(dto);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve a leave request")
    public LeaveRequestDto approve(@PathVariable Long id) {
        return leaveService.approve(id);
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a leave request")
    public LeaveRequestDto reject(@PathVariable Long id) {
        return leaveService.reject(id);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get leave requests by employee")
    public List<LeaveRequestDto> getByEmployee(@PathVariable Long employeeId) {
        return leaveService.getByEmployee(employeeId);
    }

    @GetMapping("/team-overlaps")
    @Operation(summary = "Get overlapping approved leaves for a team")
    public List<LeaveRequestDto> getOverlappingForTeam(
            @RequestParam String teamName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return leaveService.getOverlappingForTeam(teamName, startDate, endDate);
    }
}
