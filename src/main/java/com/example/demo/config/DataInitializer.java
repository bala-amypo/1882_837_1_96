package com.example.demo.config;

import com.example.demo.dto.EmployeeProfileDto;
import com.example.demo.service.EmployeeProfileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EmployeeProfileService service) {
        return args -> {
            try {
                // Check if admin already exists by trying to get a team or count
                // If your service has a way to check, use it. Otherwise, just try to create:
                EmployeeProfileDto admin = new EmployeeProfileDto();
                admin.setEmployeeId("ADM001");
                admin.setFullName("System Admin");
                admin.setEmail("admin@example.com");
                admin.setTeamName("IT");
                admin.setRole("ADMIN");
                // Note: Ensure your Service/Entity handles password encoding for "admin" or similar
                
                service.create(admin);
                System.out.println(">>> Initial Admin Created: admin@example.com");
            } catch (Exception e) {
                System.out.println(">>> Admin already exists or error: " + e.getMessage());
            }
        };
    }
}