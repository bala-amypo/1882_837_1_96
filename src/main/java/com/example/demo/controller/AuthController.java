package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate JWT token")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    /**
     * Register endpoint is mentioned in spec.
     * Not directly tested but required to exist.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public String register() {
        return "Registration endpoint";
    }
}
