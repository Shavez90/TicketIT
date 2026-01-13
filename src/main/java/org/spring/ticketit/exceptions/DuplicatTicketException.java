package org.spring.ticketit.exceptions;

public class DuplicatTicketException extends RuntimeException {
    public DuplicatTicketException(String message) {
        super(message);
    }
}
