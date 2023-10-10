package com.pa.modules.dam.model;

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
    //private String typeId = "1";
  //  private String typeString = "cow";
    private Long weight;
    @Transient
    @JsonIgnore
    private MultipartFile fileImage;

    @Column(name = "created_at")
    private String createdAt;


    @Column(name = "created_at_server", updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtServer;

    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Damdari damdari;



    @JsonIgnore
    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL )
    private List<DamStatus> damStatus;

    private int isFahli=0;

    private int hasLangesh=0;

    private int avgOfMilk=0;

    private int hasTab=0;


    @JsonIgnore
    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL)
    private List<Milking> milking;


    @JsonManagedReference
    @ManyToOne
    private Nejad nejad;

    @JsonManagedReference
    @ManyToOne
    private Type type;


    @JsonIgnore
    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL)
    private List<HistoricalTab> historicalTabList;

    @JsonIgnore
    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL)
    private List<HistoricalFahli> historicalFahliList;

    @JsonIgnore
    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL)
    private List<HistoricalLangesh> historicalLangeshList;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DamStatus lastDamStatus;
}
