package com.example.parcel_tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ParcelHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parcel_id", nullable = false)
    @JsonIgnore
    private Parcel parcel;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String status;

    private String comments;

    public ParcelHistory() {
        this.timeStamp = LocalDateTime.now();
    }

    public ParcelHistory(Parcel parcel, String location, String status) {
        this.parcel = parcel;
        this.timeStamp = LocalDateTime.now();
        this.location = location;
        this.status = status;
    }

    public ParcelHistory(Parcel parcel, String location, String status, String comments) {
        this.parcel = parcel;
        this.timeStamp = LocalDateTime.now();
        this.location = location;
        this.status = status;
        this.comments = comments;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}