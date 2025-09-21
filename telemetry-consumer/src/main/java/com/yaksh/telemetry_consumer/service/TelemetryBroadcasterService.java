// Replace the code in:
// telemetry-consumer/src/main/java/com/yaksh/telemetry_consumer/service/TelemetryBroadcasterService.java

package com.yaksh.telemetry_consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaksh.telemetry_consumer.handler.TelemetryWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TelemetryBroadcasterService {

    private static final Logger log = LoggerFactory.getLogger(TelemetryBroadcasterService.class);
    private static final String VEHICLE_KEY_PATTERN = "vehicle:*";

    private final RedisTemplate<String, Object> redisTemplate;
    private final TelemetryWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public TelemetryBroadcasterService(RedisTemplate<String, Object> redisTemplate,
                                       TelemetryWebSocketHandler webSocketHandler,
                                       ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 1000)
    public void broadcastTelemetryUpdates() {
        log.info("--- Broadcaster task started ---"); // 1. Check if the task is running at all
        try {
            Set<String> vehicleKeys = redisTemplate.keys(VEHICLE_KEY_PATTERN);
            if (vehicleKeys == null || vehicleKeys.isEmpty()) {
                log.warn("No vehicle keys found in Redis matching pattern '{}'. Nothing to broadcast.", VEHICLE_KEY_PATTERN);
                return;
            }
            log.info("Found {} vehicle keys in Redis.", vehicleKeys.size()); // 2. Check if keys are being found

            List<Map<Object, Object>> vehicleDataList = vehicleKeys.stream()
                    .map(key -> redisTemplate.opsForHash().entries(key))
                    .filter(map -> !map.isEmpty())
                    .collect(Collectors.toList());

            if (vehicleDataList.isEmpty()) {
                log.warn("Fetched keys but data maps are all empty. Nothing to broadcast.");
                return;
            }
            log.info("Successfully fetched data for {} vehicles.", vehicleDataList.size()); // 3. Check if data is being fetched

            String jsonPayload = objectMapper.writeValueAsString(vehicleDataList);
            log.info("Serialized payload: {}", jsonPayload); // 4. See the final JSON

            webSocketHandler.broadcast(jsonPayload);
            log.info("--- Broadcast successful ---");

        } catch (Exception e) {
            // This will now catch ANY exception and log it with a full stack trace
            log.error("!!! BROADCAST FAILED !!! An unexpected error occurred:", e);
        }
    }
}