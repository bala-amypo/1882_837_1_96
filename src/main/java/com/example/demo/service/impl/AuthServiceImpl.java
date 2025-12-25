package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    // CONSTRUCTOR ORDER MUST BE: (UserAccountRepository, BCryptPasswordEncoder, JwtTokenProvider)
    public AuthServiceImpl(UserAccountRepository userRepo, 
                           BCryptPasswordEncoder passwordEncoder, 
                           JwtTokenProvider tokenProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        UserAccount user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole());
    }
}