package org.spring.ticketit.exceptions;

public class TicketAlreadyAssignedException extends RuntimeException {
    public TicketAlreadyAssignedException(String message) {
        super(message);
    }
}
