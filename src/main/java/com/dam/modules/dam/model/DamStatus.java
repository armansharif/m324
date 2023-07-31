package com.dam.modules.dam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Data
public class DamStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String gPS;
    private String time;
    private Long battery;

    private Long aCCX;
    private Long aCCY;
    private Long aCCZ;
    private Long gYROX;
    private Long gYROY;
    private Long gYROZ;

    private Long pH;
    private Long activeId;
    private Long settingConf;
    private String date;
    private Long temperature;
    @Lob
    @Column(length = 1000)
    private String description;

    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne
    private Dam dam;

}
