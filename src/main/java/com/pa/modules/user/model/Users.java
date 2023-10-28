package com.pa.modules.user.model;

import com.pa.modules.committee.model.Committee;

import com.pa.modules.committee.model.MembersDTO;
import com.pa.modules.committee.model.MembershipRequest;

import com.pa.modules.location.model.District;
import com.pa.modules.notification.model.Notification;
import com.pa.modules.ticketing.model.Ticket;
import com.pa.modules.ticketing.model.TicketResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "users")
public class Users implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @Column(unique = false)
    private String email;

    private Integer baseScore;

    @Column(unique = true)
    private String mobile;


    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean enabled = true;


    private String headCode;

    //  @Column(unique = true)
    private String refCode;


    @JsonIgnore
    @Column(unique = false)
    private String username;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ref_user_id")
    private Users refUser;
    @JsonIgnore
    private String adminPassword;

    @JsonIgnore
    private String expertise;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Roles> roles = new HashSet<>();

    private String name;

    @JsonIgnore
    private String family;
    @Lob
    @Column(length = 512)
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Addresses> addresses;

    @Transient
    public Set<MembersDTO> presentedUsers;

    @JsonIgnore
    private int userType = 1;

    private String img;

    private Long education;

    private Long university;
    @Transient
    @JsonIgnore
    private MultipartFile file;


    private Long reasonSelectCommittee;
    private Integer facultyMembership;
    private Integer eliteMembership;
    private Integer gpa;
    private Integer authoredBook;
    private Integer translatedBook = 0;
    private Integer articles = 0;
    private Integer workExperience = 0;


    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    Set<MembershipRequest> membershipRequests;

    public Long getEducation() {
        return education;
    }

    public void setEducation(Long education) {
        this.education = education;
    }

    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "committee_membership",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "committee_id")})
    private Set<Committee> committee = new HashSet<>();


    @ManyToOne
    private District district;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<TicketResponse> responseList;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Ticket> ticketList;


    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    public Users() {
    }

    public Users(String mobile, String password, Set<Roles> roles) {
        this.mobile = mobile;
        this.password = password;
        this.roles = roles;
    }

    public Users(String mobile, String email, String password, Set<Roles> roles) {
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /////////////////////////////////////////////////////
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Roles roles : roles) {
            authorities.addAll(roles.getAuthorities());
        }
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadCode() {
        return headCode;
    }

    public void setHeadCode(String code) {
        this.headCode = code;
    }

    public String getRefCode() {
        return refCode;
    }


    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<Addresses> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Addresses> addresses) {
        this.addresses = addresses;
    }


    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Set<Committee> getCommittee() {
        return committee;
    }

    public void setCommittee(Set<Committee> committee) {
        this.committee = committee;
    }

    public List<TicketResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<TicketResponse> responseList) {
        this.responseList = responseList;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public Users getRefUser() {
        return refUser;
    }

    public void setRefUser(Users refUser) {
        this.refUser = refUser;
    }

    public Set<MembersDTO> getPresentedUsers() {
        return presentedUsers;
    }

    public void setPresentedUsers(Set<MembersDTO> presentedUsers) {
        this.presentedUsers = presentedUsers;
    }

    public Long getUniversity() {
        return university;
    }

    public void setUniversity(Long university) {
        this.university = university;
    }

    public Set<MembershipRequest> getMembershipRequests() {
        return membershipRequests;
    }

    public void setMembershipRequests(Set<MembershipRequest> membershipRequests) {
        this.membershipRequests = membershipRequests;
    }

    public Long getReasonSelectCommittee() {
        return reasonSelectCommittee;
    }

    public void setReasonSelectCommittee(Long reasonSelectCommittee) {
        this.reasonSelectCommittee = reasonSelectCommittee;
    }

    public Integer getFacultyMembership() {
        return facultyMembership;
    }

    public void setFacultyMembership(Integer yearOfService) {
        this.facultyMembership = yearOfService;
    }

    public Integer getEliteMembership() {
        return eliteMembership;
    }

    public void setEliteMembership(Integer eliteMembership) {
        this.eliteMembership = eliteMembership;
    }

    public Integer getGpa() {
        return gpa;
    }

    public void setGpa(Integer gpa) {
        this.gpa = gpa;
    }

    public Integer getAuthoredBook() {
        return authoredBook;
    }

    public void setAuthoredBook(Integer authoredBook) {
        this.authoredBook = authoredBook;
    }

    public Integer getTranslatedBook() {
        return translatedBook;
    }

    public void setTranslatedBook(Integer translatedBook) {
        this.translatedBook = translatedBook;
    }

    public Integer getArticles() {
        return articles;
    }

    public void setArticles(Integer articles) {
        this.articles = articles;
    }

    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Integer getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Integer baseScore) {
        this.baseScore = baseScore;
    }
}
