package org.spring.ticketit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.ticketit.dto.StatusChangeRequestDTO;
import org.spring.ticketit.dto.TicketRequestDTO;
import org.spring.ticketit.dto.TicketResponseDTO;
import org.spring.ticketit.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {




    private  final TicketService ticketService;
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")

    @PutMapping("/{ticketId}/status")
    public TicketResponseDTO changeStatus(@PathVariable String ticketId, @Valid @RequestBody StatusChangeRequestDTO request, Authentication authentication) {
String  agentEmail =    authentication.getName();
return  ticketService.changeStatus(
        ticketId,
        request.getNewStatus(),
        agentEmail);// 1 line to call service
    }
    @PreAuthorize("hasRole('USER')")

    @PostMapping
    public TicketResponseDTO createTicket(
            @Valid @RequestBody TicketRequestDTO request,
            Authentication authentication){
        String userEmail = authentication.getName();
        return ticketService.createTicket(request,userEmail);
    }
    @PreAuthorize("hasRole('USER')")

    @GetMapping("/my")
     public  List<TicketResponseDTO> getTickets(Authentication authentication){
        String  userEmail = authentication.getName();
        return ticketService.getMyTickets(userEmail);
    }
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")

    @GetMapping
    public List<TicketResponseDTO> getAllTickets(){
        return ticketService.getAllTickets();
    }
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")

    @PutMapping("/{ticketId}/assign")
    public TicketResponseDTO assignToSelf(
            @PathVariable String ticketId,
            Authentication authentication) {
        String agentEmail = authentication.getName();
        return ticketService.assignTicketToSelf(ticketId, agentEmail);
    }


}
