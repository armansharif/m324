package com.pa.modules.dam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL )
    private List<Damdari> damdari;
}
