
// package com.example.demo.service;

// import com.example.demo.dto.AuthRequest;
// import com.example.demo.dto.AuthResponse;
// import com.example.demo.model.UserAccount;

// public interface AuthService {

//     UserAccount register(UserAccount userAccount);

//     AuthResponse authenticate(AuthRequest request);
// }
package com.example.demo.service;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
        AuthResponse register(AuthRequest request); 
}