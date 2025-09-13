package com.yaksh.telemetry_consumer.service;


import com.yaksh.telemetry_consumer.model.VehicleTelemetry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelemetryConsumerService {

    private static final String VEHICLE_KEY_PREFIX = "vehicle:";

    private final RedisTemplate<String, VehicleTelemetry> redisTemplate;

    public TelemetryConsumerService(RedisTemplate<String, VehicleTelemetry> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Listens to the Kafka topic for new telemetry data.
     * This method is the entry point for all incoming messages.
     *
     * @param telemetry The VehicleTelemetry object deserialized from the Kafka message.
     */
    @KafkaListener(topics = "vehicle-telemetry", groupId = "telemetry-group")
    public void consumeTelemetry(VehicleTelemetry telemetry) {
        log.info("Received telemetry for vehicle [{}]: {}", telemetry.vehicleId(), telemetry);

        // Define the key we'll use to store this vehicle's data in Redis
        String redisKey = VEHICLE_KEY_PREFIX + telemetry.vehicleId();

        try {
            // Use the RedisTemplate to save the data.
            // opsForValue() gets the interface for simple Key-Value operations.
            // The .set() command will either create a new entry or overwrite an existing one.
            redisTemplate.opsForValue().set(redisKey, telemetry);
            log.debug("Updated Redis cache for key [{}].", redisKey);
        } catch (Exception e) {
            log.error("Error writing to Redis for key [{}]: {}", redisKey, e.getMessage());
        }
    }
}
