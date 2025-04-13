package com.example.parcel_tracker.controller;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @GetMapping("/")
    public String showIndexPage() {
        return "index.html";
    }

    @GetMapping("/create")
    public String showCreateParcelForm() {
        return "create-parcel.html";
    }

    @PostMapping("/create")
    public String createParcel(@RequestParam String initialLocation, Model model) {
        Parcel parcel = parcelService.createParcel(initialLocation);
        model.addAttribute("parcel", parcel);
        return "parcel-details.html";
    }

    @GetMapping("/track")
    public String showTrackParcelForm() {
        return "track-parcel.html";
    }

    @GetMapping("/details")
    public String getParcel(@RequestParam String trackingCode, Model model) {
        Parcel parcel = parcelService.getParcel(trackingCode);
        model.addAttribute("parcel", parcel);
        return "parcel-details.html";
    }

    @PutMapping("/update/{trackingCode}")
    public ResponseEntity<Parcel> updateParcel(@PathVariable String trackingCode, @RequestParam String currentLocation, @RequestParam ParcelStatusEnum status) {
        Parcel parcel = parcelService.updateParcel(trackingCode, currentLocation, status);
        if (parcel != null) {
            return ResponseEntity.ok(parcel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}