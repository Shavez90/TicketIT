package org.spring.ticketit.service;

import lombok.RequiredArgsConstructor;
import org.spring.ticketit.dto.RegisterRequestDTO;
import org.spring.ticketit.model.User;
import org.spring.ticketit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


        public void register(RegisterRequestDTO request) {
if(userRepository.findByEmail(request.getEmail()).isPresent()) {
    throw new RuntimeException("User already exist");
}
String password = passwordEncoder.encode(request.getPassword());
User user = User.builder().email(request.getEmail())
        .password(password)
        .role(request.getRole())
        .build();
userRepository.save(user);
};
            // 1. Check if email already exists
            // (logic goes here)

            // 2. Encode password
            // (logic goes here)

            // 3. Build User entity
            // (logic goes here)

            // 4. Save user
            // (logic goes here)
        }



