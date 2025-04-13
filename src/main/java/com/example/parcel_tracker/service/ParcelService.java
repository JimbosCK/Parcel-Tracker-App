package com.example.parcel_tracker.service;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;

    public Parcel createParcel(String initialLocation) {
        Parcel parcel = new Parcel(initialLocation);
        parcel.addHistoryEntry(initialLocation, parcel.getStatus().toString(), "Parcel created");
        return parcelRepository.save(parcel);
    }

    public Parcel getParcel(String trackingCode) {
        return parcelRepository.findByTrackingCode(trackingCode);
    }

    public Parcel updateParcel(String trackingCode, String currentLocation, ParcelStatusEnum status, String comments) {
        Parcel parcel = parcelRepository.findByTrackingCode(trackingCode);
        if (parcel != null) {
            boolean locationChanged = !parcel.getCurrentLocation().equals(currentLocation);
            boolean statusChanged = !parcel.getStatus().equals(status);

            if (locationChanged) {
                parcel.addHistoryEntry(currentLocation, status.toString(), "Location updated" + (comments != null && !comments.isEmpty() ? ": " + comments : ""));
                parcel.setCurrentLocation(currentLocation);
            }
            if (statusChanged) {
                parcel.addHistoryEntry(currentLocation, status.toString(), "Status updated" + (comments != null && !comments.isEmpty() ? ": " + comments : ""));
                parcel.setStatus(status);
            }
            return parcelRepository.save(parcel);
        }
        return null;
    }

    public Parcel updateParcel(String trackingCode, String currentLocation, ParcelStatusEnum status) {
        return updateParcel(trackingCode, currentLocation, status, null);
    }
}