package com.example.parcel_tracker.kafka.producer;

import com.example.parcel_tracker.event.ParcelStatusChangeEvent;
import com.example.parcel_tracker.model.Parcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ParcelEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ParcelEventProducer.class);

    private static final String PARCEL_STATUS_EVENTS_TOPIC = "parcel-status-events";
    private static final String PARCEL_UPDATES_TOPIC = "parcel-updates";

    private final KafkaTemplate<String, ParcelStatusChangeEvent> parcelStatusEventKafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    private final ObjectMapper objectMapper;

    public ParcelEventProducer(
            @Qualifier("parcelStatusEventKafkaTemplate") KafkaTemplate<String, ParcelStatusChangeEvent> parcelStatusEventKafkaTemplate,
            @Qualifier("stringKafkaTemplate") KafkaTemplate<String, String> stringKafkaTemplate,
            ObjectMapper objectMapper) { 
        this.parcelStatusEventKafkaTemplate = parcelStatusEventKafkaTemplate;
        this.stringKafkaTemplate = stringKafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a detailed ParcelStatusChangeEvent to the `parcel-status-events` topic.
     * This is intended for real-time aggregation
     *
     * @param event The ParcelStatusChangeEvent object containing old and new statuses.
     */
    public void sendParcelStatusChangeEvent(ParcelStatusChangeEvent event) {
        log.warn("Publishing ParcelStatusChangeEvent to topic '{}': {}", PARCEL_STATUS_EVENTS_TOPIC, event);
        try {
            parcelStatusEventKafkaTemplate.send(PARCEL_STATUS_EVENTS_TOPIC, event.getTrackingCode(), event);
        } catch (Exception e) {
            log.error("Failed to publish ParcelStatusChangeEvent for tracking code {}: {}",
                    event.getTrackingCode(), e.getMessage(), e);
        }
    }

    /**
     * Sends a generic Map<String, String> payload (as JSON string) to the `parcel-updates` topic.
     * This is intended for the email notification system.
     *
     * @param parcel The Parcel object.
     * @param eventType A descriptive string like "CREATED", "UPDATED", "DELIVERED".
     */
    public void sendGenericParcelEvent(Parcel parcel, String eventType) {
        try {
            Map<String, String> messagePayload = Map.of(
                    "trackingCode", parcel.getTrackingCode(),
                    "status", parcel.getStatus().toString(),
                    "eventType", eventType,
                    "timestamp", LocalDateTime.now().toString()
            );
            String message = objectMapper.writeValueAsString(messagePayload);
            log.warn("[{}] Publishing generic event to topic '{}': {}", eventType, PARCEL_UPDATES_TOPIC, message);
            stringKafkaTemplate.send(PARCEL_UPDATES_TOPIC, message);

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error("Error serializing parcel update event for Kafka (generic message)", e);
        } catch (Exception e) {
            log.error("Failed to publish generic parcel event for tracking code {}: {}",
                    parcel.getTrackingCode(), e.getMessage(), e);
        }
    }
}