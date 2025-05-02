package com.example.parcel_tracker.exception;

public class ParcelNotFoundException extends RuntimeException {
    public ParcelNotFoundException(String trackingCode) {
        super("Parcel with tracking code '" + trackingCode + "' not found.");
    }
}