package com.pa.modules.location.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class ElectoralDistrict implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "name", nullable = false)
    private String name;


    private String code;



    @JsonBackReference
    @OneToMany(mappedBy = "electoralDistricts", cascade = CascadeType.ALL )
    private List<District> district;


    public ElectoralDistrict(Long id) {
        this.id = id;
    }

    public ElectoralDistrict() {
    }
}
