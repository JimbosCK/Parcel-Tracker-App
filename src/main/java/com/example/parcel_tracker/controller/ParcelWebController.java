package com.example.parcel_tracker.controller;

import com.example.parcel_tracker.controller.Helpers.ParcelWebHelper;
import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelHistory;
import com.example.parcel_tracker.service.ParcelService;
import com.example.parcel_tracker.service.ShippingAddressService;
import com.example.parcel_tracker.views.ParcelUpdateView;
import com.example.parcel_tracker.repository.ParcelHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByTimeStampAsc(parcel);
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
            List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByTimeStampAsc(parcel);
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
            List<ParcelHistory> history = parcelHistoryRepository.findByParcelOrderByTimeStampAsc(parcel);
            ParcelWebHelper.addBarcodeToModel(parcel, model);
            model.addAttribute("parcel", parcel);
            model.addAttribute("history", history);
            return "parcel-details.html";
    }

    @GetMapping("/update/{trackingCode}")
    public String showUpdateForm(@PathVariable String trackingCode, Model model, RedirectAttributes redirectAttributes) {

        if (!model.containsAttribute("parcel")) {
            Parcel parcel = parcelService.getParcel(trackingCode);
            model.addAttribute("parcel", parcel);
        }
        return "update-parcel";
    }

    @PostMapping("/update/{trackingCode}")
    public String updateParcel(@PathVariable String trackingCode, @ModelAttribute ParcelUpdateView updateRequest, RedirectAttributes redirectAttributes) {
        try {
            parcelService.updateParcel(trackingCode, updateRequest);
            return "redirect:/parcel/details/" + trackingCode;
        } catch (InvalidEtaDateException e) {
            redirectAttributes.addFlashAttribute("etaError", new FieldError(
                    "parcel", "etaDate", e.getMessage()
            ));

            redirectAttributes.addFlashAttribute("comments", updateRequest.getComments());

            Parcel viewParcel = parcelService.getParcel(trackingCode);
            viewParcel.setCurrentLocation(updateRequest.getCurrentLocation());
            viewParcel.setStatus(updateRequest.getStatus());
            viewParcel.setEtaDate(updateRequest.getEtaDate());
            redirectAttributes.addFlashAttribute("parcel", viewParcel);

            return "redirect:/parcel/update/" + trackingCode;
        }
    }
    

}