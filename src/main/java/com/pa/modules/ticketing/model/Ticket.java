package com.pa.modules.ticketing.model;

import com.pa.modules.ticketing.consts.ConstTicketing;
import com.pa.modules.user.model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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

    int status = ConstTicketing.TICKET_STATUS_OPEN;

    @Lob
    @Column(length = 512)
    private String text;

    private String title;

    @JsonBackReference
    @ManyToOne
    TicketCategory ticketCategory;

    @JsonIncludeProperties(value = {"id","name","family"})
    @ManyToOne
    Users users;


    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketResponse> responseList;

    public Ticket() {
    }
}
