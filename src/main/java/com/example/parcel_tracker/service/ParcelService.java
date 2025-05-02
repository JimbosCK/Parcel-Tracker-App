package com.example.parcel_tracker.service;

import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.exception.ParcelNotFoundException;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
        Optional<Parcel> parcelOptional = parcelRepository.findByTrackingCode(trackingCode);
        if (parcelOptional.isEmpty()) {
            throw new ParcelNotFoundException(trackingCode);
        }
        return parcelOptional.get();
    }

    public Parcel updateParcel(String trackingCode, String currentLocation, ParcelStatusEnum status, String comments, LocalDate etaDate) {
        Optional<Parcel> parcelOptional = parcelRepository.findByTrackingCode(trackingCode);
            if (parcelOptional.isEmpty()) {
                throw new ParcelNotFoundException(trackingCode);
            }
            Parcel parcel = parcelOptional.get();
            if(etaDate != null && etaDate.isBefore(LocalDate.now())){
                throw new InvalidEtaDateException("Delivery date cannot be in the past!");
            }
            if (status == ParcelStatusEnum.IN_TRANSIT && etaDate == null) {
                throw new InvalidEtaDateException("Estimated Delivery Date is required when the parcel is In Transit.");
            }
            comments = (comments != null && !comments.isEmpty() ? comments : "");
            parcel.setEtaDate(etaDate);
            parcel.setCurrentLocation(currentLocation);
            parcel.setStatus(status);
            parcel.addHistoryEntry(currentLocation, status.toString(), comments);
            
            return parcelRepository.save(parcel);
    }

    public Optional<Parcel> getParcelsByID(Long id){
        return parcelRepository.findById(id);
    }

    public List<Parcel> getParcelsByStatus(ParcelStatusEnum status){
        return parcelRepository.findByStatus(status);
    }

    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

}