package org.spring.ticketit.service;

import lombok.RequiredArgsConstructor;

import org.spring.ticketit.dto.TicketRequestDTO;
import org.spring.ticketit.dto.TicketResponseDTO;
import org.spring.ticketit.enums.TicketStatus;
import org.spring.ticketit.exceptions.UserNotFoundException;
import org.spring.ticketit.model.Ticket;

import org.spring.ticketit.repository.TicketRepository;
import org.spring.ticketit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    public TicketResponseDTO createTicket(TicketRequestDTO request, String userEmail){
          userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TicketStatus.OPEN)
                .createdBy(userEmail)
                .assignedTo(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);
return mapToDto(savedTicket);

    }
    public List<TicketResponseDTO> getMyTickets(String userEmail){
        return ticketRepository.findByCreatedBy(userEmail)
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    public List<TicketResponseDTO> getAllTickets(){
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    public TicketResponseDTO assignTicketToSelf(String ticketId, String agentEmail){
          Ticket ticket= ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
          if (ticket.getAssignedTo() !=null){
              throw new RuntimeException("Already Assigned");
          }if (ticket.getStatus() != TicketStatus.OPEN) {  // ✅ ADD THIS
            throw new RuntimeException("Can only assign OPEN tickets");
        }
              ticket.setAssignedTo(agentEmail);
              ticket.setUpdatedAt(LocalDateTime.now());
              Ticket AssignedTicket = ticketRepository.save(ticket);
              ticket.setStatus(TicketStatus.IN_PROGRESS);  // ✅ ADD THIS

              return mapToDto(AssignedTicket);


    }

    public TicketResponseDTO changeStatus(String ticketId, TicketStatus newStatus, String agentEmail) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getAssignedTo() == null) {
            throw new RuntimeException("This ticket not assigned yet");
        }

        if (!ticket.getAssignedTo().equals(agentEmail)) {
            throw new RuntimeException("Wrong agent this ticket is assigned to another agent");
        }
        if (!isValidTransition(ticket.getStatus(),newStatus)){
            throw new RuntimeException("INVALID TRANSITION");
        }

        ticket.setStatus(newStatus);           // ✅ ADD THIS - Actually update status
        ticket.setUpdatedAt(LocalDateTime.now());  // ✅ ADD THIS - Update timestamp

 Ticket savedTicket = ticketRepository.save(ticket);
        return  mapToDto(savedTicket);

    }


    /// ///////////////////Validation////////////////////////

    private boolean isValidTransition(TicketStatus currentStatus, TicketStatus newStatus) {
if (  currentStatus.equals(TicketStatus.OPEN)){
     return newStatus.equals( TicketStatus.IN_PROGRESS);

}
        if (  currentStatus.equals(TicketStatus.IN_PROGRESS)){
            return newStatus.equals(TicketStatus.RESOLVED) ;

        }
        if (  currentStatus.equals(TicketStatus.RESOLVED)){
            return newStatus.equals(TicketStatus.CLOSED);

        }
        // start thinking from here

        return false;
    }

    ///////////////////// DTO MAPPER /////////////////////
    private TicketResponseDTO mapToDto(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .createdBy(ticket.getCreatedBy())
                .assignedTo(ticket.getAssignedTo())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();

    }
}
/*
public TicketResponseDTO create(String userId,  TicketRequestDTO request) {
        Ticket  ticket = new Ticket();
        ticket.setId(userId);
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        return mapper.toDTO(journalRepository.save(journal));*/