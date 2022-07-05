package com.video.modules.video.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    //get from config
    @Transient
    @JsonIgnore
    private static String domain = "http://158.58.185.117:8080";

    @Transient
    @JsonIgnore
    private MultipartFile fileImage;

    @Transient
    @JsonIgnore
    private MultipartFile fileVideo;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String city;


    @JsonManagedReference
    @OneToMany(mappedBy = "video")
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}
