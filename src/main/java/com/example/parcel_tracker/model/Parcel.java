package com.example.parcel_tracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String trackingCode;

    @Column(nullable = false)
    private String currentLocation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParcelStatusEnum status;

    private LocalDateTime lastUpdated;

    private LocalDate etaDate;

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelHistory> history = new ArrayList<>();

    public Parcel() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Parcel(String initialLocation) {
        this.trackingCode = generateTrackingCode();
        this.currentLocation = initialLocation;
        this.status = ParcelStatusEnum.PENDING;
        this.lastUpdated = LocalDateTime.now();
    }

    public Parcel(String initialLocation, LocalDate etaDate) {
        this.trackingCode = generateTrackingCode();
        this.currentLocation = initialLocation;
        this.status = ParcelStatusEnum.PENDING;
        this.lastUpdated = LocalDateTime.now();
        this.etaDate = etaDate;
    }

    // Getters and Setters
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
        this.lastUpdated = LocalDateTime.now();
    }

    public ParcelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ParcelStatusEnum status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<ParcelHistory> getHistory() {
        return history;
    }

    public void setHistory(List<ParcelHistory> history) {
        this.history = history;
    }

    public void addHistoryEntry(String location, String status, String comments) {
        ParcelHistory entry = new ParcelHistory(this, location, status, comments);
        this.history.add(entry);
    }

    public void addHistoryEntry(String location, String status) {
        ParcelHistory entry = new ParcelHistory(this, location, status);
        this.history.add(entry);
    }

    private String generateTrackingCode() {
        return java.util.UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    public LocalDate getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(LocalDate etaDate) {
        this.etaDate = etaDate;
    }
}