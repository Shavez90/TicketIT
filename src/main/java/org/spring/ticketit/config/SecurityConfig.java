package org.spring.ticketit.config;






import lombok.RequiredArgsConstructor;
import org.spring.ticketit.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth

                            // 🔓 Public endpoints
                            .requestMatchers("/api/auth/**").permitAll()

                            // 👤 USER endpoints
                            .requestMatchers(HttpMethod.POST, "/api/tickets").hasRole("USER")
                            .requestMatchers(HttpMethod.GET, "/api/tickets/my").hasRole("USER")

                            // 🧑‍💼 AGENT / ADMIN endpoints
                            .requestMatchers(HttpMethod.GET, "/api/tickets").hasAnyRole("AGENT", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/tickets/**/assign").hasAnyRole("AGENT", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/tickets/**/status").hasAnyRole("AGENT", "ADMIN")

                            // 🔐 Everything else
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


            return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
                throws Exception {
            return config.getAuthenticationManager();
        }

    }

/*
KEY CONCEPTS:
- SessionCreationPolicy.STATELESS → No server-side sessions, JWT only
- jwtAuthFilter executes BEFORE Spring's default username/password filter
- CSRF disabled (safe for stateless APIs with JWT)
- AuthenticationManager bean required for login endpoint
*/

