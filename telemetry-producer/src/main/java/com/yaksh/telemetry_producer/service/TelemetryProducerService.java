package com.yaksh.telemetry_producer.service;


import com.yaksh.telemetry_producer.config.KafkaProducerConfig;
import com.yaksh.telemetry_producer.model.VehicleTelemetry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelemetryProducerService {


    // Spring Boot automatically configures this template for us based on our application.yml
    private final KafkaTemplate<String, VehicleTelemetry> kafkaTemplate;

    public TelemetryProducerService(KafkaTemplate<String, VehicleTelemetry> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a VehicleTelemetry object to our Kafka topic.
     *
     * @param telemetry The telemetry data to send.
     */
    public void sendTelemetry(VehicleTelemetry telemetry) {
        // The 'send' method is asynchronous. It returns a CompletableFuture if we want to
        // handle success or failure, but for this fire-and-forget scenario, we won't.
        kafkaTemplate.send(KafkaProducerConfig.TELEMETRY_TOPIC, telemetry.vehicleId(), telemetry);
        log.info("Sent telemetry data for vehicle [{}]: {}", telemetry.vehicleId(), telemetry);
    }
}
