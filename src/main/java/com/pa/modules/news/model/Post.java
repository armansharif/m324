package com.pa.modules.news.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pa.modules.location.model.City;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String title;
    private String subtitle;
    private String date;

    private String img;

    private String video;
    private int likeCount;

    private int dislikeCount;

    private int doubleLikeCount;

    private int doubleDislikeCount;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL )
    private List<Likes> likes;

    @Transient
    private int isLiked=0;
    @Transient
    private int isDisliked=0;
    @Transient
    private int isDoubleLiked=0;
    @Transient
    private int isDoubleDisliked=0;


    private int duration;

    private double coef;

    @Lob
    @Column( length = 100000 )
    private String content;

    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "post_category",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL )
    private List<Images> images;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int disLikeCount) {
        this.dislikeCount = disLikeCount;
    }

    public int getDoubleLikeCount() {
        return doubleLikeCount;
    }

    public void setDoubleLikeCount(int doubleLikeCount) {
        this.doubleLikeCount = doubleLikeCount;
    }

    public int getDoubleDislikeCount() {
        return doubleDislikeCount;
    }

    public void setDoubleDislikeCount(int doubleDsLikeCount) {
        this.doubleDislikeCount = doubleDsLikeCount;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public int getIsDisliked() {
        return isDisliked;
    }

    public void setIsDisliked(int isDisliked) {
        this.isDisliked = isDisliked;
    }

    public int getIsDoubleLiked() {
        return isDoubleLiked;
    }

    public void setIsDoubleLiked(int isDoubleLiked) {
        this.isDoubleLiked = isDoubleLiked;
    }

    public int getIsDoubleDisliked() {
        return isDoubleDisliked;
    }

    public void setIsDoubleDisliked(int isDoubleDisliked) {
        this.isDoubleDisliked = isDoubleDisliked;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(List<Likes> likes) {
        this.likes = likes;
    }
}
