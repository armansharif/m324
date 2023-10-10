package com.pa.modules.ticketing.service;

import com.pa.commons.Consts;
import com.pa.modules.ticketing.model.Ticket;
import com.pa.modules.ticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAllOpenTicket(String sort,
                                          int page,
                                          int perPage
    ) {
        Pageable sortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());
        return ticketRepository.findAllByStatus(Consts.TICKET_STATUS_OPEN, sortedAndPagination);
    }

    public Ticket findTicketOfUser(String user_id) {
        return new Ticket();
    }
}
