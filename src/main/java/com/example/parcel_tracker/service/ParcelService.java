package com.example.parcel_tracker.service;

import com.example.parcel_tracker.event.ParcelStatusChangeEvent;
import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.exception.ParcelNotFoundException;
import com.example.parcel_tracker.kafka.producer.ParcelEventProducer;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.model.ShippingAddress;
import com.example.parcel_tracker.repository.ParcelRepository;
import com.example.parcel_tracker.repository.ShippingAddressRepository;
import com.example.parcel_tracker.views.ParcelUpdateView;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ParcelEventProducer parcelEventProducer; 

    public ParcelService(ParcelRepository parcelRepository, ShippingAddressRepository shippingAddressRepository, ParcelEventProducer parcelEventProducer) {
        this.parcelRepository = parcelRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.parcelEventProducer = parcelEventProducer;
    }


    public Parcel createParcel(String initialLocation, String comment) {
        Parcel parcel = new Parcel(initialLocation);
        parcel.addHistoryEntry(initialLocation, parcel.getStatus().toString(), comment);
        Parcel savedParcel = parcelRepository.save(parcel);
        parcelEventProducer.sendParcelStatusChangeEvent(new ParcelStatusChangeEvent(
            savedParcel.getId(),
            savedParcel.getTrackingCode(),
            null,
            savedParcel.getStatus(),
            LocalDateTime.now()
        ));

        parcelEventProducer.sendGenericParcelEvent(savedParcel, "CREATED");
        
        return savedParcel;
    }

    public Parcel createParcel(String initialLocation, String comment, ShippingAddress shippingAddress) {
        shippingAddressRepository.save(shippingAddress);
        Parcel parcel = new Parcel(initialLocation, shippingAddress);
        parcel.addHistoryEntry(initialLocation, parcel.getStatus().toString(), comment);
        Parcel savedParcel = parcelRepository.save(parcel);
        parcelEventProducer.sendParcelStatusChangeEvent(new ParcelStatusChangeEvent(
            savedParcel.getId(),
            savedParcel.getTrackingCode(),
            null,
            savedParcel.getStatus(),
            LocalDateTime.now()
        ));        
        parcelEventProducer.sendGenericParcelEvent(savedParcel, "CREATED");

        return savedParcel;
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

        ParcelStatusEnum oldStatus = parcel.getStatus();

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
        
        Parcel updatedParcel = parcelRepository.save(parcel);

        if (!oldStatus.equals(updatedParcel.getStatus())) {
            parcelEventProducer.sendParcelStatusChangeEvent(new ParcelStatusChangeEvent(
                updatedParcel.getId(),
                updatedParcel.getTrackingCode(),
                oldStatus,
                updatedParcel.getStatus(),
                LocalDateTime.now()
            ));
        }

        parcelEventProducer.sendGenericParcelEvent(updatedParcel, "UPDATED");

        return updatedParcel;
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