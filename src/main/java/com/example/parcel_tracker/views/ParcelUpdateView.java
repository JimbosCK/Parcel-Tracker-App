package com.example.parcel_tracker.views;

import java.time.LocalDate;
import com.example.parcel_tracker.model.ParcelStatusEnum;

public class ParcelUpdateView {
    private String currentLocation;
    private ParcelStatusEnum status;
    private String comments;
    private LocalDate etaDate;

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(LocalDate etaDate) {
        this.etaDate = etaDate;
    }
}