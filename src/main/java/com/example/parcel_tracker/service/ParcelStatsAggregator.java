package com.example.parcel_tracker.service;

import com.example.parcel_tracker.event.ParcelStatusChangeEvent;
import com.example.parcel_tracker.model.ParcelStatusEnum;
import com.example.parcel_tracker.repository.ParcelRepository;
import com.example.parcel_tracker.websocket.ParcelStatsWebSocketSender;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ParcelStatsAggregator {

    private static final Logger log = LoggerFactory.getLogger(ParcelStatsAggregator.class);

    private final Map<ParcelStatusEnum, AtomicInteger> statusCounts = new ConcurrentHashMap<>();
    private final ParcelStatsWebSocketSender webSocketSender;
    private final ParcelRepository parcelRepository;

    public ParcelStatsAggregator(ParcelStatsWebSocketSender webSocketSender, ParcelRepository parcelRepository) {
        this.webSocketSender = webSocketSender;
        this.parcelRepository = parcelRepository;
    }

    @PostConstruct
    public void initializeCounts() {
        log.warn("Initializing parcel status counts from database...");

        Arrays.stream(ParcelStatusEnum.values()).forEach(status ->
            statusCounts.put(status, new AtomicInteger(0))
        );

        List<Object[]> dbCountsList = parcelRepository.countParcelsByStatus();

        Map<ParcelStatusEnum, Long> dbCounts = dbCountsList.stream()
            .collect(Collectors.toMap(
                row -> (ParcelStatusEnum) row[0], 
                row -> (Long) row[1]              
            ));

        dbCounts.forEach((status, count) -> {
            statusCounts.computeIfPresent(status, (key, val) -> {
                val.set(count.intValue());
                return val;
            });
        });

        log.warn("Initialized parcel status counts from DB: {}", statusCounts);
        broadcastCurrentStats(); 
    }

    @KafkaListener(
        topics = "parcel-status-events",
        groupId = "parcel-stats-aggregator-group",
        containerFactory = "parcelStatusEventKafkaListenerContainerFactory"
    )
    public void listenForParcelStatusChanges(ParcelStatusChangeEvent event) {
        log.warn("Received Kafka event for stats: {}", event);

        if (event.getOldStatus() != null) {
            statusCounts.computeIfPresent(event.getOldStatus(), (key, val) -> {
                val.decrementAndGet();
                return val;
            });
        }

        if (event.getNewStatus() != null) {
            statusCounts.computeIfPresent(event.getNewStatus(), (key, val) -> {
                val.incrementAndGet();
                return val;
            });
        }
        
        log.warn("Updated parcel status counts: {}", statusCounts);
        broadcastCurrentStats();
    }

    public Map<String, Integer> getCurrentStats() {
        Map<String, Integer> currentSnapshot = new HashMap<>();
        statusCounts.forEach((status, count) ->
            currentSnapshot.put(status.name(), count.get())
        );
        return Collections.unmodifiableMap(currentSnapshot);
    }

    private void broadcastCurrentStats() {
        webSocketSender.sendParcelStats(getCurrentStats());
    }
}