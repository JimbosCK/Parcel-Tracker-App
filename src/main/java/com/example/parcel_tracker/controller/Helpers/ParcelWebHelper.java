package com.example.parcel_tracker.controller.Helpers;

import java.io.IOException;
import java.util.Base64;

import org.springframework.ui.Model;

import com.example.parcel_tracker.model.Parcel;
import com.google.zxing.WriterException;

public class ParcelWebHelper {

    public static void  addBarcodeToModel(Parcel parcel, Model model) {
        BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
        try {
            byte[] barcodeImage = barcodeGenerator.generateCode128BarcodeImage(parcel.getTrackingCode(), 600, 150);
            String base64Image = Base64.getEncoder().encodeToString(barcodeImage);
            model.addAttribute("barcodeImage", "data:image/png;base64," + base64Image);
        } catch (WriterException | IOException e) {
            model.addAttribute("barcodeImageError", "Error generating barcode.");
        }
    }
    
}
