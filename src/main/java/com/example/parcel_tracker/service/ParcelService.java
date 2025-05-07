package com.example.parcel_tracker.service;

import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.exception.ParcelNotFoundException;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.model.ShippingAddress;
import com.example.parcel_tracker.repository.ParcelRepository;
import com.example.parcel_tracker.repository.ShippingAddressRepository;
import com.example.parcel_tracker.views.ParcelUpdateView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    public Parcel createParcel(String initialLocation, String comment) {
        Parcel parcel = new Parcel(initialLocation);
        parcel.addHistoryEntry(initialLocation, parcel.getStatus().toString(), comment);
        return parcelRepository.save(parcel);
    }

    public Parcel createParcel(String initialLocation, String comment, ShippingAddress shippingAddress) {
        shippingAddressRepository.save(shippingAddress);
        Parcel parcel = new Parcel(initialLocation, shippingAddress);
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

    public Parcel updateParcel(String trackingCode, ParcelUpdateView updateRequest) {
        Optional<Parcel> parcelOptional = parcelRepository.findByTrackingCode(trackingCode);
        if (parcelOptional.isEmpty()) {
            throw new ParcelNotFoundException(trackingCode);
        }
        Parcel parcel = parcelOptional.get();
        if(updateRequest.getEtaDate() != null && updateRequest.getEtaDate().isBefore(LocalDate.now())){
            throw new InvalidEtaDateException("Delivery date cannot be in the past!");
        }
        if (updateRequest.getStatus() == ParcelStatusEnum.IN_TRANSIT && updateRequest.getEtaDate() == null) {
            throw new InvalidEtaDateException("Estimated Delivery Date is required when the parcel is In Transit.");
        }
        updateRequest.setComments( (updateRequest.getComments() != null && !updateRequest.getComments().isEmpty() ? updateRequest.getComments() : ""));
        
        parcel.setEtaDate(updateRequest.getEtaDate());
            parcel.setCurrentLocation(updateRequest.getCurrentLocation());
            parcel.setStatus(updateRequest.getStatus());
            parcel.addHistoryEntry(updateRequest.getCurrentLocation(), updateRequest.getStatus().toString(), updateRequest.getComments());
            
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