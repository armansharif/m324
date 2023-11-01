package com.pa.modules.ticketing.controller;

import com.pa.commons.Routes;
import com.pa.modules.ticketing.model.Ticket;
import com.pa.modules.ticketing.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
public class TicketController {


    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(value = {Routes.GET_tickets})
    public List<Ticket> getTicketsByToken(HttpServletRequest request) {

        return ticketService.findTicketOfUser(request);
    }


    @GetMapping(value = {Routes.GET_admin_tickets})
    public List<Ticket> getTickets(HttpServletRequest request, @RequestParam(required = false, defaultValue = "id") String sort,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "10") int perPage) {
        return ticketService.findAllOpenTicket(sort, page, perPage);
    }

    @PostMapping(value = {Routes.GET_tickets_add})
    public Ticket addTicket(HttpServletRequest request,
                            @RequestParam Long categoryId,
                            @RequestParam String title,
                            @RequestParam String content
    ) {
        return ticketService.addTicket(request, categoryId, title, content);
    }

    @PostMapping(value = {Routes.GET_tickets_close})
    public Ticket closeTicket(HttpServletRequest request, @PathVariable Long id) {
        return ticketService.closeTicket(request, id);
    }

    @PostMapping(value = {Routes.GET_tickets_response})
    public Ticket addResponseTicket(HttpServletRequest request,
                                    @PathVariable Long id,
                                    @RequestParam String response) {
        return ticketService.addResponseTicket(request, id, response);
    }
}
