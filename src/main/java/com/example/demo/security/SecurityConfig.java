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
                    .csrf(csrf -> csrf.disable()) // השבתת CSRF
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**" ,"/api/users").permitAll() // גישה פתוחה לסווגר
//                          //  .requestMatchers(HttpMethod.GET, "/api/users").permitAll() // גישה פתוחה לנתיב זה
//                            //.requestMatchers(HttpMethod.POST, "/api/auth/update-passwords").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .anyRequest().authenticated() // כל הבקשות האחרות דורשות אימות
                    );

            return http.build();
        }
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
}
