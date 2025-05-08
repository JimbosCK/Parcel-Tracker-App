package com.example.parcel_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
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

    private LocalDateTime creationDate;

    private LocalDate etaDate;

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelHistory> history = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;

    public Parcel() {
        this.creationDate = LocalDateTime.now();
    }

    public Parcel(String initialLocation) {
        this.trackingCode = generateTrackingCode();
        this.currentLocation = initialLocation;
        this.status = ParcelStatusEnum.PENDING;
        this.creationDate = LocalDateTime.now();
    }

    public Parcel(String initialLocation, ShippingAddress shippingAddress) {
        this.trackingCode = generateTrackingCode();
        this.currentLocation = initialLocation;
        this.status = ParcelStatusEnum.PENDING;
        this.creationDate = LocalDateTime.now();
        this.shippingAddress = shippingAddress;
    }

    public Parcel(String initialLocation, LocalDate etaDate, ShippingAddress shippingAddress) {
        this.trackingCode = generateTrackingCode();
        this.currentLocation = initialLocation;
        this.status = ParcelStatusEnum.PENDING;
        this.creationDate = LocalDateTime.now();
        this.etaDate = etaDate;
        this.shippingAddress = shippingAddress;
    }

    //Methods
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
    
}