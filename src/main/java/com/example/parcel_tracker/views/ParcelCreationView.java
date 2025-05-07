package com.example.parcel_tracker.views;

import com.example.parcel_tracker.model.ParcelStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class ParcelCreationView {
    @NotBlank(message = "Current location is required")
    private String currentLocation;
    private ParcelStatusEnum status;
    private String comments;
    private LocalDate etaDate;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String recipientPhone;
    @Email(message = "Invalid email format")
    private String recipientEmail;
    private String recipientNotes;
    @NotBlank(message = "Street address is required")
    private String recipientStreet;
    @NotBlank(message = "City is required")
    private String recipientCity;
    @NotBlank(message = "Zip code is required")
    private String recipientZipCode;
    @NotBlank(message = "Country is required")
    private String recipientCountry;

    // getters, and setters

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ParcelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ParcelStatusEnum status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(LocalDate etaDate) {
        this.etaDate = etaDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientNotes() {
        return recipientNotes;
    }

    public void setRecipientNotes(String recipientNotes) {
        this.recipientNotes = recipientNotes;
    }

    public String getRecipientStreet() {
        return recipientStreet;
    }

    public void setRecipientStreet(String recipientStreet) {
        this.recipientStreet = recipientStreet;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public void setRecipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
    }

    public String getRecipientZipCode() {
        return recipientZipCode;
    }

    public void setRecipientZipCode(String recipientZipCode) {
        this.recipientZipCode = recipientZipCode;
    }

    public String getRecipientCountry() {
        return recipientCountry;
    }

    public void setRecipientCountry(String recipientCountry) {
        this.recipientCountry = recipientCountry;
    }
}