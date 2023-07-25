package com.dam.modules.dam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
public class DamStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String gPS;
    private String time;
    private Long battery;

    private Long aCCX;
    private Long aCCY;
    private Long aCCZ;
    private Long gYROX;
    private Long gYROY;
    private Long gYROZ;

    private Long pH;
    private Long activeId;
    private Long settingConf;
    private String date;
    private Long temperature;
    @Lob
    @Column(length = 1000)
    private String description;

    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @JsonBackReference
    @ManyToOne
    private Dam dam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getgPS() {
        return gPS;
    }

    public void setgPS(String gPS) {
        this.gPS = gPS;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getBattery() {
        return battery;
    }

    public void setBattery(Long title) {
        this.battery = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long address) {
        this.temperature = address;
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

    public Dam getDam() {
        return dam;
    }

    public void setDam(Dam dam) {
        this.dam = dam;
    }

    public Long getaCCX() {
        return aCCX;
    }

    public void setaCCX(Long aCCX) {
        this.aCCX = aCCX;
    }

    public Long getaCCY() {
        return aCCY;
    }

    public void setaCCY(Long aCCY) {
        this.aCCY = aCCY;
    }

    public Long getaCCZ() {
        return aCCZ;
    }

    public void setaCCZ(Long aCCZ) {
        this.aCCZ = aCCZ;
    }

    public Long getgYROX() {
        return gYROX;
    }

    public void setgYROX(Long gYROX) {
        this.gYROX = gYROX;
    }

    public Long getgYROY() {
        return gYROY;
    }

    public void setgYROY(Long gYROY) {
        this.gYROY = gYROY;
    }

    public Long getgYROZ() {
        return gYROZ;
    }

    public void setgYROZ(Long gYROZ) {
        this.gYROZ = gYROZ;
    }

    public Long getpH() {
        return pH;
    }

    public void setpH(Long pH) {
        this.pH = pH;
    }

    public Long getActiveId() {
        return activeId;
    }

    public void setActiveId(Long activeId) {
        this.activeId = activeId;
    }

    public Long getSettingConf() {
        return settingConf;
    }

    public void setSettingConf(Long settingConf) {
        this.settingConf = settingConf;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
