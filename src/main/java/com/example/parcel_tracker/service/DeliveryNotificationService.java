package com.example.parcel_tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeliveryNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryNotificationService.class);

    public void sendDeliveryNotification(String trackingCode) {
        logger.warn("Simulating sending email notification for parcel with tracking code: {}", trackingCode);
        // TODO : Replace with real email sender like JavaMailSender or SendGrid etc
    }
}