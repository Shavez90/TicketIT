package org.spring.ticketit.repository;

import org.spring.ticketit.model. Ticket;
import org.springframework. data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByCreatedBy(String email);  // Find tickets created by a user


    List<Ticket> findByAssignedTo(String email);  // Find tickets assigned to an agent

    
    Optional<Ticket> findById(String s);
    boolean existsByTitle(String title);
}