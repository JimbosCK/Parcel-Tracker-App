package com.example.parcel_tracker.controller;

import com.example.parcel_tracker.controller.Helpers.ParcelWebHelper;
import com.example.parcel_tracker.exception.InvalidEtaDateException;
import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelHistory;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.model.ShippingAddress;
import com.example.parcel_tracker.service.ParcelService;
import com.example.parcel_tracker.service.ParcelStatsAggregator;
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
import java.util.Map;


@Controller
@RequestMapping("/parcel")
public class ParcelWebController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private ParcelHistoryRepository parcelHistoryRepository; 

    @Autowired
    private ParcelStatsAggregator parcelStatsAggregator;


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
    public String createNewParcel(@Valid @ModelAttribute ParcelCreationView parcelCreationView, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "create-parcel.html";
        }

        ShippingAddress shippingAddress = new ShippingAddress(
            parcelCreationView.getRecipientName(),
            parcelCreationView.getRecipientPhone(),
            parcelCreationView.getRecipientEmail(),
            parcelCreationView.getRecipientNotes(),
            parcelCreationView.getRecipientStreet(),
            parcelCreationView.getRecipientCity(),
            parcelCreationView.getRecipientZipCode(),
            parcelCreationView.getRecipientCountry()
        );

        Parcel parcel = parcelService.createParcel(
            parcelCreationView.getCurrentLocation(),
            parcelCreationView.getComments(),
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
            updateView.setShippingAddress(parcel.getShippingAddress());
            model.addAttribute("updateRequest", updateView);
        }
        model.addAttribute("statuses", ParcelStatusEnum.values());
        return "update-parcel";
    }

    @PostMapping("/update/{trackingCode}")
    public String updateParcel(@PathVariable String trackingCode, @ModelAttribute ParcelUpdateView parcelUpdateView, RedirectAttributes redirectAttributes) {
        try {
            parcelService.updateParcel(trackingCode, parcelUpdateView);
            return "redirect:/parcel/details/" + trackingCode;
        } catch (InvalidEtaDateException e) {
            redirectAttributes.addFlashAttribute("etaError", new FieldError(
                    "parcel", "etaDate", e.getMessage()
            ));

            redirectAttributes.addFlashAttribute("comments", parcelUpdateView.getComments());

            Parcel viewParcel = parcelService.getParcel(trackingCode);
            viewParcel.setCurrentLocation(parcelUpdateView.getCurrentLocation());
            viewParcel.setStatus(parcelUpdateView.getStatus());
            viewParcel.setEtaDate(parcelUpdateView.getEtaDate());
            redirectAttributes.addFlashAttribute("parcel", viewParcel);

            return "redirect:/parcel/update/" + trackingCode;
        }
    }

    @GetMapping("/controlpanel")
    public String controlPanel(Model model) {
        Map<String, Integer> initialStats = parcelStatsAggregator.getCurrentStats();
        model.addAttribute("initialStats", initialStats);
        return "control-panel";
    }
    

}