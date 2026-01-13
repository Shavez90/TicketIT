package org.spring.ticketit.exceptions;

import org.spring.ticketit.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(
            UserAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTicketNotFound(
            TicketNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(TicketAlreadyAssignedException.class)
    public ResponseEntity<Map<String, String>> handleTicketAlreadyAssigned(
            TicketAlreadyAssignedException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(DuplicatTicketException.class)
    public ResponseEntity<Map<String, String>> handleTicketAlreadyCreated(
            DuplicatTicketException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(InvalidTicketStatusException.class)
    public ResponseEntity<Map<String, String>> handleTicketStatus(
            InvalidTicketStatusException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }


// ================= VALIDATION ERRORS =================

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
            .getFieldErrors()
            .forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
}

// ================= AUTHENTICATION ERRORS =================

@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<Map<String, String>> handleBadCredentials(
        BadCredentialsException ex) {

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Invalid email or password"));
}

@ExceptionHandler(AuthenticationException.class)
public ResponseEntity<Map<String, String>> handleAuthenticationException(
        AuthenticationException ex) {

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Authentication failed"));
}

// ================= AUTHORIZATION ERRORS =================

@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<Map<String, String>> handleAccessDenied(
        AccessDeniedException ex) {

    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(Map.of("error", "Access denied"));
}

// ================= FALLBACK (LAST) =================

@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, String>> handleGenericException(
        Exception ex) {
    ex.printStackTrace(); // ← THIS is critical! It dumps the real error/stack trace

    return ResponseEntity

            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Something went wrong = GL"));
}


}