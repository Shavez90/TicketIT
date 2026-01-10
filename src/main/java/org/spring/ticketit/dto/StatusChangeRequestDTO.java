package org.spring.ticketit.dto;



import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.spring.ticketit.enums.TicketStatus;

@Data                  // Generates getters, setters, toString, equals, hashCode
@AllArgsConstructor   // Constructor with all 8 fields
@NoArgsConstructor


public class StatusChangeRequestDTO {
@NotNull
    private TicketStatus newStatus;
}
