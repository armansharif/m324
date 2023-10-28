package com.pa.modules.location.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pa.modules.user.model.Users;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class District implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "name", nullable = false)
    private String name;


    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL )
    private List<Users> user;


    @JsonIgnore
    @ManyToOne
    private City city;

    @JsonManagedReference
    @ManyToOne
    private ElectoralDistrict  electoralDistricts;

    public District(Long id) {
        this.id = id;
    }

    public District() {
    }


}
