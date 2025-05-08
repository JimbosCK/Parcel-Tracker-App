package com.example.parcel_tracker.views;

import com.example.parcel_tracker.model.ShippingAddress;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import java.time.LocalDate;
import lombok.Data;


@Data
public class ParcelUpdateView {
    private String currentLocation;
    private ParcelStatusEnum status;
    private String comments;
    private LocalDate etaDate;
    private ShippingAddress shippingAddress;

}