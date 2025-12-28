package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                // 1. Keep your custom server URL
                .servers(List.of(
                        new Server().url("https://9209.408procr.amypo.ai/")
                ))
                // 2. Add Info (Title/Version)
                .info(new Info()
                        .title("Leave Management API")
                        .version("1.0")
                        .description("API for managing employee leaves and team capacity"))
                // 3. Add the "Lock" icon / Security Requirement to all endpoints
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                // 4. Define HOW the security works (JWT Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}