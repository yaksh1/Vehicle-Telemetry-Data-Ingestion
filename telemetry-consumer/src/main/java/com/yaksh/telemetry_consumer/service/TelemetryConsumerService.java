// com/yaksh/telemetry_consumer/service/TelemetryConsumerService.java (Updated)

package com.yaksh.telemetry_consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaksh.telemetry_consumer.model.VehicleTelemetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TelemetryConsumerService {

    private static final String VEHICLE_KEY_PREFIX = "vehicle:";

    // The template is now more generic
    private final RedisTemplate<String, Object> redisTemplate;

    // Inject ObjectMapper to convert the POJO to a Map
    private final ObjectMapper objectMapper;

    public TelemetryConsumerService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Listens to the Kafka topic and saves telemetry data as a Redis Hash.
     *
     * @param telemetry The VehicleTelemetry object deserialized from the Kafka message.
     */
    @KafkaListener(topics = "vehicle-telemetry", groupId = "telemetry-group")
    public void consumeTelemetry(VehicleTelemetry telemetry) {
        log.info("Received telemetry(T) for vehicle [{}]: {}", telemetry.vehicleId(), telemetry);
        String redisKey = VEHICLE_KEY_PREFIX + telemetry.vehicleId();

        try {
            // Convert the VehicleTelemetry object into a Map
            Map<String, Object> telemetryMap = objectMapper.convertValue(telemetry, Map.class);

            // Use opsForHash() and putAll() to save the map as a Redis Hash.
            // This will create or overwrite the entire hash at the specified key.
            redisTemplate.opsForHash().putAll(redisKey, telemetryMap);

            log.debug("Updated Redis Hash for key [{}].", redisKey);
        } catch (Exception e) {
            log.error("Error writing to Redis Hash for key [{}]: {}", redisKey, e.getMessage());
        }
    }
}