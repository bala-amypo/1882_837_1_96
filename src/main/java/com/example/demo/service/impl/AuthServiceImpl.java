
// package com.example.demo.service.impl;

// import com.example.demo.dto.AuthRequest;
// import com.example.demo.dto.AuthResponse;
// import com.example.demo.exception.BadRequestException;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.model.UserAccount;
// import com.example.demo.repository.UserAccountRepository;
// import com.example.demo.service.AuthService;
// import org.springframework.stereotype.Service;

// @Service
// public class AuthServiceImpl implements AuthService {

//     private final UserAccountRepository userRepo;

//     public AuthServiceImpl(UserAccountRepository userRepo) {
//         this.userRepo = userRepo;
//     }

//     @Override
//     public UserAccount register(UserAccount userAccount) {

//         // Store password as-is (NO hashing)
//         return userRepo.save(userAccount);
//     }

//     @Override
//     public AuthResponse authenticate(AuthRequest request) {

//         UserAccount user = userRepo.findByEmail(request.getEmail())
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//         // Plain-text password comparison
//         if (!request.getPassword().equals(user.getPassword())) {
//             throw new BadRequestException("Invalid credentials");
//         }

//         AuthResponse response = new AuthResponse();
//         response.setToken("dummy-token"); // No JWT
//         response.setUserId(user.getId());
//         response.setEmail(user.getEmail());
//         response.setRole(user.getRole());

//         return response;
//     }
// }

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
       @Override
    public AuthResponse register(AuthRequest request) {
        // Check if the email already exists
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        // Create new UserAccount instance
        UserAccount newUser = new UserAccount();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));  // Encode the password
        newUser.setRole("USER");  // Default role (you can change this as needed)

        // Save the new user to the database
        userRepo.save(newUser);

        // Generate a JWT token for the new user
        String token = tokenProvider.generateToken(newUser);

        // Return the AuthResponse
        return new AuthResponse(token, newUser.getId(), newUser.getEmail(), newUser.getRole());
    }
}
