package com.example.parcel_tracker.service;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;

    public Parcel createParcel(String initialLocation, String comment) {
        Parcel parcel = new Parcel(initialLocation);
        parcel.addHistoryEntry(initialLocation, parcel.getStatus().toString(), comment);
        return parcelRepository.save(parcel);
    }

    public Parcel getParcel(String trackingCode) {
        return parcelRepository.findByTrackingCode(trackingCode);
    }

    public Parcel updateParcel(String trackingCode, String currentLocation, ParcelStatusEnum status, String comments) {
        Parcel parcel = parcelRepository.findByTrackingCode(trackingCode);
        if (parcel != null) {
            comments = (comments != null && !comments.isEmpty() ? comments : "");
            
            parcel.setCurrentLocation(currentLocation);
            parcel.setStatus(status);
            parcel.addHistoryEntry(currentLocation, status.toString(), comments);
            
            return parcelRepository.save(parcel);
        }
        return null;
    }

    public List<Parcel> getParcelsByStatus(ParcelStatusEnum status){
        return parcelRepository.findByStatus(status);
    }

}