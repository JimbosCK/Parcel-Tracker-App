package com.example.parcel_tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String trackingCode;
    private String currentLocation;
    @Enumerated(EnumType.STRING)
    private ParcelStatusEnum status;
    private LocalDateTime lastUpdated;

    // Constructors, getters, setters
    public Parcel() {}

    public Parcel(String trackingCode, String currentLocation, ParcelStatusEnum status, LocalDateTime lastUpdated) {
        this.trackingCode = trackingCode;
        this.currentLocation = currentLocation;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ParcelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ParcelStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}