package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**" ,"/api/users").permitAll()
                            .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                            .anyRequest().authenticated()
                    );
            return http.build();
        }
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
}
