package com.dam.modules.dam.model;

import com.fasterxml.jackson.annotation.*;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
public class Dam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String photo;
    private String birthdate;


    private String typeId = "1";


    private String typeString = "cow";


    @Transient
    @JsonIgnore
    private MultipartFile fileImage;

    @Column(name = "created_at")
    private String createdAt;


    @Column(name = "created_at_server", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAtServer;

    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Damdari damdari;



    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL)
    private List<DamStatus> damStatus;

    private int isFahli=0;

    private int hasLangesh=0;

    private int avgOfShirdehi=0;

}
