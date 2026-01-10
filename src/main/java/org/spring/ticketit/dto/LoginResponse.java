package org.spring.ticketit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.spring.ticketit.model.Role;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private Role role;
}
