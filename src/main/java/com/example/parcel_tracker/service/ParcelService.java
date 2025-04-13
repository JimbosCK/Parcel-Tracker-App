package com.example.parcel_tracker.service;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.repository.ParcelRepository;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;

    public Parcel createParcel(String initialLocation) {
        String trackingCode = UUID.randomUUID().toString();
        Parcel parcel = new Parcel(trackingCode, initialLocation, ParcelStatusEnum.PENDING, LocalDateTime.now());
        return parcelRepository.save(parcel);
    }

    public Parcel updateParcel(String trackingCode, String currentLocation, ParcelStatusEnum status) {
        Parcel parcel = parcelRepository.findByTrackingCode(trackingCode);
        if (parcel != null) {
            parcel.setCurrentLocation(currentLocation);
            parcel.setStatus(status);
            parcel.setLastUpdated(LocalDateTime.now());
            return parcelRepository.save(parcel);
        }
        return null;
    }

    public Parcel getParcel(String trackingCode) {
        return parcelRepository.findByTrackingCode(trackingCode);
    }
}
