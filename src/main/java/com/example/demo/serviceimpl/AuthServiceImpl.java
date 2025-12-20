package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserAccountRepository repo,
                           BCryptPasswordEncoder encoder,
                           JwtTokenProvider tokenProvider) {
        this.repo = repo;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse authenticate(AuthRequest req) {

        UserAccount user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = tokenProvider.generateToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
