package org.spring.ticketit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.ticketit.dto.StatusChangeRequestDTO;
import org.spring.ticketit.dto.TicketRequestDTO;
import org.spring.ticketit.dto.TicketResponseDTO;
import org.spring.ticketit.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {




    private  final TicketService ticketService;
    @PutMapping("/{ticketId}/status")
    public TicketResponseDTO changeStatus(@PathVariable String ticketId, @Valid @RequestBody StatusChangeRequestDTO request, Authentication authentication) {
String  agentEmail =    authentication.getName();
return  ticketService.changeStatus(
        ticketId,
        request.getNewStatus(),
        agentEmail);// 1 line to call service
    }
    @PostMapping
    public TicketResponseDTO createTicket(
            @Valid @RequestBody TicketRequestDTO request,
            Authentication authentication){
        String userEmail = authentication.getName();
        return ticketService.createTicket(request,userEmail);
    }

    @GetMapping("/my")
     public  List<TicketResponseDTO> getTickets(Authentication authentication){
        String  userEmail = authentication.getName();
        return ticketService.getMyTickets(userEmail);
    }

    @GetMapping
    public List<TicketResponseDTO> getAllTickets(){
        return ticketService.getAllTickets();
    }

    @PutMapping("/{ticketId}/assign")
    public TicketResponseDTO assignToSelf(
            @PathVariable String ticketId,
            Authentication authentication) {
        String agentEmail = authentication.getName();
        return ticketService.assignTicketToSelf(ticketId, agentEmail);
    }


}
