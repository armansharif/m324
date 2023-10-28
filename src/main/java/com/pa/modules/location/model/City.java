package com.pa.modules.location.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pa.modules.user.model.Users;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "name", nullable = false)
    private String name;


    private String code;


    @JsonIgnore
    @ManyToOne
    private State state;




    @JsonBackReference
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL )
    private List<District> district;

    public City(Long id) {
        this.id = id;
    }

    public City() {
    }
}
