package com.example.parcel_tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String notes;

    private String street;
    private String city;
    private String zipCode;
    private String country;
    
    public ShippingAddress(String name, String phone, String email, String notes, String street, String city, String zipCode, String country) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.notes = notes;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", "  + " " + zipCode + ", " + country;
    }

}
