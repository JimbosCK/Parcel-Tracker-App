package com.example.parcel_tracker.views;

import com.example.parcel_tracker.model.ShippingAddress;

import jakarta.validation.constraints.NotBlank;

import com.example.parcel_tracker.model.ParcelStatusEnum;
import java.time.LocalDate;
import lombok.Data;


@Data
public class ParcelUpdateView {
    @NotBlank(message = "Current location is required")
    private String currentLocation;
    private ParcelStatusEnum status;
    private String comments;
    private LocalDate etaDate;
    private ShippingAddress shippingAddress;

}