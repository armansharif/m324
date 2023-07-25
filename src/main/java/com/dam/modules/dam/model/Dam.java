package com.dam.modules.dam.model;

import com.fasterxml.jackson.annotation.*;

import com.dam.modules.user.model.Users;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Dam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String photo;
    private Long age;


    @Transient
    @JsonIgnore
    private static String typeId = "";


    @JsonIgnore
    private static String typeString = "";


    @Transient
    @JsonIgnore
    private MultipartFile fileImage;

    @Column(name = "created_at")
    private String createdAt;


    @Column(name = "created_at_server", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAtServer;

    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    private boolean isChecked = false;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Users users;


    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "dam", cascade = CascadeType.ALL)
    private List<DamStatus> damStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String deviceId) {
        this.name = deviceId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String background_image_url) {
        this.photo = background_image_url;
    }

    public static String getTypeId() {
        return typeId;
    }

    public static void setTypeId(String typeId) {
        Dam.typeId = typeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAtServer() {
        return createdAtServer;
    }

    public void setCreatedAtServer(LocalDateTime createdAtServer) {
        this.createdAtServer = createdAtServer;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public MultipartFile getFileImage() {
        return fileImage;
    }

    public void setFileImage(MultipartFile fileImage) {
        this.fileImage = fileImage;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @JsonProperty("user_id")
    @Transient
    public Long getOwnerId() {
        return users == null ? null : users.getId();
    }

    public static String getTypeString() {
        return typeString;
    }

    public static void setTypeString(String typeString) {
        Dam.typeString = typeString;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<DamStatus> getDamStatus() {
        return damStatus;
    }

    public void setDamStatus(List<DamStatus> damStatus) {
        this.damStatus = damStatus;
    }


}
