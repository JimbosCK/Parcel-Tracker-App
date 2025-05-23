package com.example.parcel_tracker.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ParcelStatsWebSocketSender {

    private static final Logger log = LoggerFactory.getLogger(ParcelStatsWebSocketSender.class);

    private final SimpMessagingTemplate messagingTemplate;

    public ParcelStatsWebSocketSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends the current parcel statistics to all subscribers of the /topic/parcel-stats endpoint.
     * @param stats A map where keys are ParcelStatusEnum names (String) and values are Integer counts.
     */
    public void sendParcelStats(Map<String, Integer> stats) {
        log.warn("Sending parcel stats to WebSocket: {}", stats);
        messagingTemplate.convertAndSend("/topic/parcel-stats", stats);
    }
}