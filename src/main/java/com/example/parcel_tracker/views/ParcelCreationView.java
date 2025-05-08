package com.example.parcel_tracker.views;

import com.example.parcel_tracker.model.ParcelStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
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

}