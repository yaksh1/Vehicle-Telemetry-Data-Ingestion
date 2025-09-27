// com/yaksh/telemetry_consumer/service/TelemetryConsumerService.java (Updated)

package com.yaksh.telemetry_consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaksh.telemetry_consumer.entity.TelemetryLogEntity;
import com.yaksh.telemetry_consumer.model.VehicleTelemetry;
import com.yaksh.telemetry_consumer.repository.TelemetryLogRepository;
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

    private final TelemetryLogRepository telemetryLogRepository;

    public TelemetryConsumerService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, TelemetryLogRepository telemetryLogRepository) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.telemetryLogRepository = telemetryLogRepository;
    }

    /**
     * Listens to the Kafka topic and saves telemetry data as a Redis Hash.
     *
     * @param telemetry The VehicleTelemetry object deserialized from the Kafka message.
     */
    @KafkaListener(topics = "vehicle-telemetry", groupId = "telemetry-group")
    public void consumeTelemetry(VehicleTelemetry telemetry) {
        log.info("Received telemetry for vehicle [{}]: {}", telemetry.vehicleId(), telemetry);

        // --- 1. Existing Logic: Update Redis Cache (for the live dashboard) ---
        String redisKey = VEHICLE_KEY_PREFIX + telemetry.vehicleId();
        try {
            Map<String, Object> telemetryMap = objectMapper.convertValue(telemetry, Map.class);
            redisTemplate.opsForHash().putAll(redisKey, telemetryMap);
            log.debug("Updated Redis Hash for key [{}].", redisKey);
        } catch (Exception e) {
            log.error("Error writing to Redis Hash for key [{}]: {}", redisKey, e.getMessage());
        }

        // --- 2. New Logic: Persist to PostgreSQL (for historical data) ---
        try {
            TelemetryLogEntity logEntity = new TelemetryLogEntity();
            // Map data from the incoming message to the entity
            logEntity.setVehicleId(telemetry.vehicleId());
            logEntity.setLatitude(telemetry.latitude());
            logEntity.setLongitude(telemetry.longitude());
            logEntity.setSpeed(telemetry.speed());
            logEntity.setFuelLevel(telemetry.fuelLevel());
            logEntity.setTimestamp(telemetry.timestamp());

            telemetryLogRepository.save(logEntity);
            log.debug("Saved telemetry to PostgreSQL for vehicle [{}].", telemetry.vehicleId());
        } catch (Exception e) {
            log.error("Error writing to PostgreSQL for vehicle [{}]: {}", telemetry.vehicleId(), e.getMessage());
        }
    }
}