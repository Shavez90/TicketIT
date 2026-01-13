package org.spring.ticketit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.ticketit.dto.LoginRequest;
import org.spring.ticketit.dto.LoginResponse;
import org.spring.ticketit.dto.RegisterRequestDTO;
import org.spring.ticketit.model.Role;
import org.spring.ticketit.security.JwtService;
import org.spring.ticketit.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        // 1️⃣ Authenticate credentials (email + password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();


        // 2️⃣ Extract authenticated user identity (email)
        String email = authentication.getName();

        // 3️⃣ Extract role from authorities
        String authority = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();   // e.g. "ROLE_USER"

        // 4️⃣ Convert authority → Role enum
        Role role = Role.valueOf(authority.replace("ROLE_", ""));

        // 5️⃣ Generate JWT using authenticated principal

        String token = jwtService.generateToken(userDetails);

        // 6️⃣ Return identity-focused response
        return ResponseEntity.ok(
                new LoginResponse(token, email, role)
        );
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

}
