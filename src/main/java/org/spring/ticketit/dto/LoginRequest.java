package org.spring.ticketit.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.spring.ticketit.model.Role;
@Data
@RequiredArgsConstructor
public class LoginRequest {

        private String email;
        private String password;
        private Role role;  // Optional, default to USER

}
