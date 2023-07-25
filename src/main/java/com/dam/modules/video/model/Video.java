package com.dam.modules.video.model;

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
public class Video implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String backgroundImageUrl;
    private String videoUrl;
    private String csvUrl;
    //get from config
    @Transient
    @JsonIgnore
    private static String domain = "http://158.58.185.117:8080";



    @JsonIgnore
    private static String videoFileName = "";


    @Transient
    @JsonIgnore
    private MultipartFile fileImage;

    @Transient
    @JsonIgnore
    private MultipartFile fileVideo;

    @Column(name = "created_at")
    private String createdAt;


    @Column(name = "created_at_server", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAtServer;

    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String city;

    private boolean isChecked = false;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Users users;


    @JsonManagedReference
    @OneToMany(mappedBy = "video",cascade = CascadeType.ALL)
    private List<LocationPath> locationPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String background_image_url) {
        this.backgroundImageUrl = background_image_url;
    }

    public static String getDomain() {
        return domain;
    }

    public static void setDomain(String domain) {
        Video.domain = domain;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    public List<LocationPath> getLocationPath() {
        return locationPath;
    }

    public void setLocationPath(List<LocationPath> locationPath) {
        this.locationPath = locationPath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String video_url) {
        this.videoUrl = video_url;
    }

    public MultipartFile getFileImage() {
        return fileImage;
    }

    public void setFileImage(MultipartFile fileImage) {
        this.fileImage = fileImage;
    }

    public MultipartFile getFileVideo() {
        return fileVideo;
    }

    public void setFileVideo(MultipartFile fileVideo) {
        this.fileVideo = fileVideo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public static String getVideoFileName() {
        return videoFileName;
    }

    public static void setVideoFileName(String videoFileName) {
        Video.videoFileName = videoFileName;
    }

    public String getCsvUrl() {
        return csvUrl;
    }

    public void setCsvUrl(String csvUrl) {
        this.csvUrl = csvUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
