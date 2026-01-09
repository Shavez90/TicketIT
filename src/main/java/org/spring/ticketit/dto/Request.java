package org.spring.ticketit.dto;

import org.spring.ticketit.model.Role;

public class Request {

        private String email;
        private String password;
        private Role role;  // Optional, default to USER

}
