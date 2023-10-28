package com.pa.modules.committee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pa.modules.user.model.Users;

import javax.persistence.*;

@Entity
public class MembershipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Transient
    int isAcceptable = 0;

    @Transient
    int isRejectable = 0;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "committee_id")
    Committee committee;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
//    @JoinTable(name = "request_committee_membership",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "request_id")
//    )
//    private Set<Users> user = new HashSet<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL )
//    private List<Committee> committees;
//

    private int status;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Committee getCommittee() {
        return committee;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    public int getIsAcceptable() {
        return isAcceptable;
    }

    public void setIsAcceptable(int isAcceptable) {
        this.isAcceptable = isAcceptable;
    }

    public int getIsRejectable() {
        return isRejectable;
    }

    public void setIsRejectable(int isRejectable) {
        this.isRejectable = isRejectable;
    }
}
