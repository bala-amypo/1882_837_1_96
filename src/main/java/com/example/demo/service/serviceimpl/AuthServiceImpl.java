package com.example.demo.service.serviceimpl; // Must match the folder name exactly

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
// ... (rest of imports)

@Service
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserAccountRepository userRepo, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        UserAccount user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new com.example.demo.exception.BadRequestException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new com.example.demo.exception.BadRequestException("Invalid credentials");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole());
    }
}