package com.example.parcel_tracker.controller;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelHistory;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.service.ParcelService;
import com.example.parcel_tracker.repository.ParcelHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private ParcelHistoryRepository parcelHistoryRepository; 

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
        if (parcel != null) {
            List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByDateTimeAsc(parcel);
            model.addAttribute("parcel", parcel);
            model.addAttribute("history", history); // Add the history to the model
            return "parcel-details.html";
        } else {
            model.addAttribute("parcel", null);
            return "parcel-details.html";
        }
    }

    @PutMapping("/update/{trackingCode}")
    public ResponseEntity<Parcel> updateParcel(@PathVariable String trackingCode, @RequestParam String currentLocation, @RequestParam ParcelStatusEnum status, @RequestParam(required = false) String comments) {
        Parcel parcel = parcelService.updateParcel(trackingCode, currentLocation, status, comments);
        if (parcel != null) {
            return ResponseEntity.ok(parcel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}