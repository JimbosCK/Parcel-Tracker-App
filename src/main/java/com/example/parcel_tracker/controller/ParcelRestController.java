package com.example.parcel_tracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.service.ParcelService;

@RestController
@RequestMapping("/api/parcels")
public class ParcelRestController {
    @Autowired
    private final ParcelService parcelService;

    public ParcelRestController(ParcelService parcelService){
        this.parcelService = parcelService;
    }

    @GetMapping
    public ResponseEntity<List<Parcel>> getAllParcels() {
        return ResponseEntity.ok(parcelService.getAllParcels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parcel> getParcelByID(@PathVariable Long id) {
        Optional<Parcel> parcel = parcelService.getParcelsByID(id);

        return parcel.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/list")
    public List<Parcel> postMethodName(@RequestParam ParcelStatusEnum status) {
        List<Parcel> parcels = parcelService.getParcelsByStatus(status);
        
        return parcels;
    }
}
