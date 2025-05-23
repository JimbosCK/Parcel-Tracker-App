package com.example.parcel_tracker.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.example.parcel_tracker.model.ParcelStatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelStatusChangeEvent {
    private Long parcelId;
    private String trackingCode;
    private ParcelStatusEnum oldStatus;
    private ParcelStatusEnum newStatus;
    private LocalDateTime timestamp;
}