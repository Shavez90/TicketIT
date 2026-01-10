package org.spring.ticketit.model;

import lombok.*;
import org.spring.ticketit.enums. TicketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    private String id;

    private String title;

    private String description;

    private TicketStatus status;

    private String createdBy;  // Email of USER who created the ticket

    private String assignedTo;  // Email of AGENT (null if unassigned)

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}