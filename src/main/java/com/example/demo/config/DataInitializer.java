package com.example.demo.config;

import com.example.demo.entity.EmployeeProfile; // Adjust package as needed
import com.example.demo.repository.EmployeeRepository; // Adjust package as needed
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                EmployeeProfile admin = new EmployeeProfile();
                admin.setFullName("System Admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin")); // Hash the password!
                admin.setRole("ADMIN");
                admin.setEmployeeId("ADM001");
                admin.setTeamName("IT");
                
                repository.save(admin);
                System.out.println(">>> Default Admin Created: admin@example.com / admin");
            }
        };
    }
}