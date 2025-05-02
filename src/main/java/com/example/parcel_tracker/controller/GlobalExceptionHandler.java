package com.example.parcel_tracker.controller;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.exception.ParcelNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEtaDateException.class)
    public String handleInvalidEtaDateException(InvalidEtaDateException ex, Model model) {
        model.addAttribute("etaError", new FieldError(
                "parcel", "etaDate", ex.getMessage()
        ));
        return "update-parcel";
    }

    @ExceptionHandler(ParcelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleParcelNotFoundException(ParcelNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "track-parcel";
    }
}