package com.pa.modules.ticketing.model;

import com.pa.commons.Consts;
import com.pa.modules.user.model.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    int status = Consts.TICKET_STATUS_OPEN;

    @Lob
    @Column(length = 512)
    private String text;

    @ManyToOne
    TicketCategory ticketCategory;

    @ManyToOne
    Users users;

    @JsonIgnore
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketResponse> responseList;

}
