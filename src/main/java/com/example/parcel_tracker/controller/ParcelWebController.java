package com.example.parcel_tracker.controller;

import com.example.parcel_tracker.controller.Helpers.ParcelWebHelper;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelHistory;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.service.ParcelService;
import com.example.parcel_tracker.repository.ParcelHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/parcel")
public class ParcelWebController {

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
    public String createParcel(@RequestParam String initialLocation, @RequestParam String comment, Model model) {
        Parcel parcel = parcelService.createParcel(initialLocation, comment);
        List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByDateTimeAsc(parcel);
        
        ParcelWebHelper.addBarcodeToModel(parcel, model);
        model.addAttribute("parcel", parcel);
        model.addAttribute("history", history);
        return "parcel-details.html";
    }

    @GetMapping("/track")
    public String showTrackParcelForm() {
        return "track-parcel.html";
    }

    @GetMapping("/details")
    public String getParcel(@RequestParam String trackingCode, Model model) {
        Parcel parcel = parcelService.getParcel(trackingCode.trim());
        if (parcel != null) {
            List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByDateTimeAsc(parcel);
            ParcelWebHelper.addBarcodeToModel(parcel, model);
            model.addAttribute("parcel", parcel);
            model.addAttribute("history", history);
            return "parcel-details.html";
        } else {
            model.addAttribute("parcel", null);
            return "parcel-details.html";
        }
    }

    @GetMapping("/details/{trackingCode}")
    public String showParcelDetails(@PathVariable String trackingCode, Model model) {
        Parcel parcel = parcelService.getParcel(trackingCode.trim());
        if (parcel != null) {
            List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByDateTimeAsc(parcel);
            ParcelWebHelper.addBarcodeToModel(parcel, model);
            model.addAttribute("parcel", parcel);
            model.addAttribute("history", history);
            return "parcel-details.html";
        } else {
            model.addAttribute("errorMessage", "Parcel with tracking code " + trackingCode + " not found.");
            return "parcel-track"; 
        }
    }

    @GetMapping("/update/{trackingCode}")
    public String showUpdateParcelForm(@PathVariable String trackingCode, Model model) {
        Parcel parcel = parcelService.getParcel(trackingCode.trim());
        if (parcel != null) {
            model.addAttribute("parcel", parcel);
            return "update-parcel.html";
        } else {
            model.addAttribute("parcel", null);
            return "update-parcel.html"; 
        }
}

@PostMapping("/update/{trackingCode}")
    public RedirectView updateParcel(@PathVariable String trackingCode, @RequestParam String currentLocation, 
    @RequestParam ParcelStatusEnum status, @RequestParam(required = false) String comments, @RequestParam(required = false) LocalDate etaDate) {
        Parcel parcel = parcelService.updateParcel(trackingCode.trim(), currentLocation, status, comments, etaDate);
        if (parcel != null) {
            return new RedirectView("/parcel/details/" + trackingCode.trim());
        } else {
            return new RedirectView("/parcel/track?error=notfound");
        }
    }
    
}