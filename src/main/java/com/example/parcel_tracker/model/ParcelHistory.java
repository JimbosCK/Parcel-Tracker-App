package com.example.parcel_tracker.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;


@Data
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
}