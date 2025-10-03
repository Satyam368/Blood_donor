package com.example.blood_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless for API
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers(
                                "/api/signup",           // User registration
                                "/api/users/signup",     // Alternative signup endpoint
                                "/api/users/login",      // User login
                                "/api/donors",           // Donor registration and browsing
                                "/api/find-donors/**",   // Find donors functionality
                                "/api/users",            // Get all users (for admin/testing)
                                "/api/users/**",         // User profile access
                                "/h2-console/**",        // H2 database console (development only)
                                "/actuator/**"           // Spring Boot actuator endpoints
                        ).permitAll()
                        
                        // Protected endpoints - authentication required
                        .requestMatchers(
                                "/api/admin/**",         // Admin functionality
                                "/api/secure/**"         // Any secure endpoints
                        ).authenticated()
                        
                        // All other requests are permitted for now (can be changed to authenticated() for stricter security)
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Allow H2 console to work
                );

        return http.build();
    }
}
