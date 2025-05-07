package com.example.parcel_tracker.controller;

import com.example.parcel_tracker.controller.Helpers.ParcelWebHelper;
import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelHistory;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.model.ShippingAddress;
import com.example.parcel_tracker.service.ParcelService;
import com.example.parcel_tracker.views.ParcelCreationView;
import com.example.parcel_tracker.views.ParcelUpdateView;

import jakarta.validation.Valid;

import com.example.parcel_tracker.repository.ParcelHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String showCreateParcelForm(Model model) {
        model.addAttribute("parcelCreationView", new ParcelCreationView());
        model.addAttribute("statuses", ParcelStatusEnum.values());
        return "create-parcel.html";
    }

    @PostMapping("/create")
    public String createNewParcel(@Valid @ModelAttribute ParcelCreationView request, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "create-parcel.html";
        }

        ShippingAddress shippingAddress = new ShippingAddress(
                request.getRecipientName(),
                request.getRecipientPhone(),
                request.getRecipientEmail(),
                request.getRecipientNotes(),
                request.getRecipientStreet(),
                request.getRecipientCity(),
                request.getRecipientZipCode(),
                request.getRecipientCountry()
        );

        Parcel parcel = parcelService.createParcel(
                request.getCurrentLocation(),
                request.getComments(),
                shippingAddress
        );

        redirectAttributes.addFlashAttribute("message", "Parcel created successfully with tracking code: " + parcel.getTrackingCode());
        return "redirect:/parcel/details/" + parcel.getTrackingCode();
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
        if (!model.containsAttribute("updateRequest")) {
            Parcel parcel = parcelService.getParcel(trackingCode);
            ParcelUpdateView updateView = new ParcelUpdateView();
            updateView.setCurrentLocation(parcel.getCurrentLocation());
            updateView.setStatus(parcel.getStatus());
            updateView.setEtaDate(parcel.getEtaDate());
            updateView.setShippingAddress(parcel.getShippingAddress()); // Populate shipping address
            model.addAttribute("updateRequest", updateView);
        }
        model.addAttribute("statuses", ParcelStatusEnum.values());
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