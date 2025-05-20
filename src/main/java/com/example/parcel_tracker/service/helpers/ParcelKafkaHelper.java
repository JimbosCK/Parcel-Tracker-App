package com.example.parcel_tracker.service.helpers;

import com.example.parcel_tracker.model.Parcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ParcelKafkaHelper {

    private static final Logger logger = LoggerFactory.getLogger(ParcelKafkaHelper.class);
    private static final String PARCEL_UPDATES_TOPIC = "parcel-updates";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendParcelEvent(Parcel parcel, String eventType) {
        try {
            Map<String, String> messagePayload = Map.of(
                    "trackingCode", parcel.getTrackingCode(),
                    "status", parcel.getStatus().toString(),
                    "eventType", eventType,
                    "timestamp", LocalDateTime.now().toString()
            );
            String message = objectMapper.writeValueAsString(messagePayload);
            logger.warn("[{}] Publishing to '{}': {}", eventType, PARCEL_UPDATES_TOPIC, message);
            kafkaTemplate.send(PARCEL_UPDATES_TOPIC, message);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Error serializing parcel update event for Kafka", e);
        }
    }
}