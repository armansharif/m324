package com.pa.modules.ticketing.service;

import com.pa.commons.toDelete.UserServiceException;
import com.pa.modules.ticketing.consts.ConstTicketing;
import com.pa.modules.ticketing.model.Ticket;
import com.pa.modules.ticketing.model.TicketCategory;
import com.pa.modules.ticketing.model.TicketResponse;
import com.pa.modules.ticketing.repository.TicketCategoryRepository;
import com.pa.modules.ticketing.repository.TicketRepository;
import com.pa.modules.ticketing.repository.TicketResponseRepository;
import com.pa.modules.user.model.Users;
import com.pa.modules.user.repository.UsersRepository;
import com.pa.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Service

public class TicketService {

    private TicketRepository ticketRepository;
    private TicketResponseRepository ticketResponseRepository;
    private UserService userService;

    private MessageSource messageSource;
    private TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketResponseRepository ticketResponseRepository, UsersRepository usersRepository, UserService userService, MessageSource messageSource, TicketCategoryRepository ticketCategoryRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketResponseRepository = ticketResponseRepository;
        this.userService = userService;
        this.messageSource = messageSource;
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    public List<Ticket> findAllOpenTicket(String sort,
                                          int page,
                                          int perPage
    ) {
        Pageable sortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());
        return ticketRepository.findAllByStatus(ConstTicketing.TICKET_STATUS_OPEN, sortedAndPagination);
    }

    public List<Ticket> findTickets(HttpServletRequest request) {
        return ticketRepository.findAll();
    }

    public List<Ticket> findTicketOfUser(HttpServletRequest request) {


        Users user = userService.getUserByToken(request);
        return ticketRepository.findAllByUsers(user);
    }

    public Ticket addTicket(HttpServletRequest request, Long categoryId, String title, String content) {
        Users user = userService.getUserByToken(request);
        TicketCategory category = ticketCategoryRepository.findById(categoryId).orElseThrow(RuntimeException::new);

        Ticket ticket = new Ticket();
        ticket.setStatus(ConstTicketing.TICKET_STATUS_OPEN);
        ticket.setText(content);
        ticket.setTitle(title);
        ticket.setTicketCategory(category);
        ticket.setUsers(user);

        return ticketRepository.save(ticket);
    }

    public Ticket closeTicket(HttpServletRequest request, Long ticketId) {
        Users user = userService.getUserByToken(request);
        //check user owner of ticket or admin
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(RuntimeException::new);
        ticket.setStatus(ConstTicketing.TICKET_STATUS_CLOSED);
        return ticketRepository.save(ticket);
    }

    public Ticket addResponseTicket(HttpServletRequest request, Long ticketId, String responseText) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(RuntimeException::new);

        if (ticket.getStatus() == ConstTicketing.TICKET_STATUS_CLOSED) {
            //      throw new UserServiceException(messageSource.getMessage("ticket.closed",null,Locale.getDefault()));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, messageSource.getMessage("ticket.closed", null, Locale.getDefault()));
        }
        // check user is admin Or user is ownerOf ticket

        Users user = userService.getUserByToken(request);

        if (user.getId() == ticket.getUsers().getId()) {
            ticket.setStatus(ConstTicketing.TICKET_STATUS_WAITING_FOR_SUPPORTER);
        } else {
            ticket.setStatus(ConstTicketing.TICKET_STATUS_WAITING_FOR_USER);

        }

        ticketRepository.save(ticket);
        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setTicket(ticket);
        ticketResponse.setText(responseText);
        ticketResponse.setUsers(user);
        ticketResponseRepository.saveAndFlush(ticketResponse);
        return ticketRepository.findById(ticketId).orElseThrow(RuntimeException::new);
    }


}
