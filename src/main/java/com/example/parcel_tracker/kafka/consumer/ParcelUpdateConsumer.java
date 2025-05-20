package com.example.parcel_tracker.kafka.consumer;

import com.example.parcel_tracker.service.DeliveryNotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ParcelUpdateConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ParcelUpdateConsumer.class);
    private static final String PARCEL_UPDATES_TOPIC = "parcel-updates";

    @Autowired
    private DeliveryNotificationService deliveryNotificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = PARCEL_UPDATES_TOPIC, groupId = "delivery-notifier-group")
    public void listenParcelUpdates(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            String trackingCode = root.path("trackingCode").asText();
            String newStatus = root.path("status").asText();
            String eventType = root.path("eventType").asText();

            logger.warn("Received Type={}: Tracking Code={}, New Status={}",eventType, trackingCode, newStatus);

            if ("DELIVERED".equalsIgnoreCase(newStatus)) {
                deliveryNotificationService.sendDeliveryNotification(trackingCode);
            }
        } catch (IOException e) {
            logger.error("Error processing Kafka message: {}", message, e);
        }
    }
}