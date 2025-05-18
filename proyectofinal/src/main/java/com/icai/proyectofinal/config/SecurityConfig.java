package com.icai.proyectofinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desactiva CSRF (solo en desarrollo)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite cualquier petición sin autenticación
                );

        return http.build();
    }
}
