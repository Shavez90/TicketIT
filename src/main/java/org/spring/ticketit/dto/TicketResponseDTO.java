package org.spring.ticketit.dto;

import lombok.*;
import org.spring.ticketit.enums.TicketStatus;


import java.time.LocalDateTime;
@Data                  // Generates getters, setters, toString, equals, hashCode
@Builder              // Generates builder pattern
@AllArgsConstructor   // Constructor with all 8 fields
@NoArgsConstructor    // Constructor with no arguments
public class TicketResponseDTO {


    private String id;

    private String title;

    private String description;

    private TicketStatus status;

    private String createdBy;  // Email of USER who created the ticket

    private String assignedTo;  // Email of AGENT (null if unassigned)

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
