package com.example.parcel_tracker.exception;

public class InvalidEtaDateException extends RuntimeException {
    public InvalidEtaDateException(String message) {
        super(message);
    }
}