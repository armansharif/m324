package com.pa.modules.news.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pa.modules.user.model.Users;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private Set<Post> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private Set<Category> childCategories;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "damdari")
    private Set<Users> users = new HashSet<>();

}