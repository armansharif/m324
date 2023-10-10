package com.pa.modules.ticketing.service;

import com.pa.modules.ticketing.repository.TicketResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketResponseService {
    private TicketResponseRepository ticketResponseRepository;

    @Autowired
    public TicketResponseService(TicketResponseRepository ticketResponseRepository) {
        this.ticketResponseRepository = ticketResponseRepository;
    }
}
