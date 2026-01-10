package org.spring.ticketit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.spring.ticketit.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

@NotBlank// for string
@Email
private String email;
@NotBlank//for strings
private String  password;
@NotNull
private Role role;
}
