package com.pa.modules.committee.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pa.modules.user.model.Users;
import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class Committee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_commission", nullable = false)
    private int isCommission;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Committee parent;


    @JsonIgnore
    @OneToMany(mappedBy = "committee")
    Set<MembershipRequest>  membershipRequests;

    @OneToMany(mappedBy = "parent")
    @OrderBy("id")
    private Set<Committee> childCommittees;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "committee")
    private Set<Users> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    @Transient
    public Set<MembersDTO>  members;
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsCommission() {
        return isCommission;
    }

    public void setIsCommission(int isCommission) {
        this.isCommission = isCommission;
    }

    public Committee getParent() {
        return parent;
    }

    public void setParent(Committee parent) {
        this.parent = parent;
    }

    public Set<Committee> getChildCommittees() {
        return childCommittees;
    }

    public void setChildCommittees(Set<Committee> childCommittees) {
        this.childCommittees = childCommittees;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public Set<MembersDTO> getMembers() {
        Set<MembersDTO> members = new HashSet<>();
        Set<Users> users = this.getUsers();
        for (Users u : users) {
            MembersDTO member = new MembersDTO();
            if (u.getName() != null)
                member.setFullName(u.getName());
            if (!u.getRoles().isEmpty())
                member.setRoleName(u.getRoles().iterator().next().getName());
            member.setUserId(u.getId());
            members.add(member);
        }
        return members;
    }

    public void setMembers(Set<MembersDTO> members) {
        this.members = members;
    }

    public Set<MembershipRequest> getMembershipRequests() {
        return membershipRequests;
    }

    public void setMembershipRequests(Set<MembershipRequest> membershipRequests) {
        this.membershipRequests = membershipRequests;
    }
}