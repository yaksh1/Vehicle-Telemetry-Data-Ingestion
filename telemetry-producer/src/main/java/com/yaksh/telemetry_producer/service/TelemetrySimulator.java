package com.yaksh.telemetry_producer.service;

import com.yaksh.telemetry_producer.model.VehicleTelemetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TelemetrySimulator {

    private static final Logger log = LoggerFactory.getLogger(TelemetrySimulator.class);
    private static final int FLEET_SIZE = 5; // Let's simulate 5 vehicles

    private final TelemetryProducerService producerService;
    private final List<String> vehicleIds = new ArrayList<>();
    private final Random random = new Random();

    public TelemetrySimulator(TelemetryProducerService producerService) {
        this.producerService = producerService;
        // Create a static list of vehicle IDs for our fleet
        for (int i = 0; i < FLEET_SIZE; i++) {
            vehicleIds.add("vehicle-" + (i + 1));
        }
        log.info("Initialized telemetry simulator for a fleet of {} vehicles.", FLEET_SIZE);
    }

    /**
     * This method is scheduled to run every second to simulate a vehicle sending data.
     */
    @Scheduled(fixedRate = 1000)
    public void generateAndSendTelemetry() {
        // Pick a random vehicle from our fleet to send an update
        String vehicleId = vehicleIds.get(random.nextInt(FLEET_SIZE));

        // Simulate some realistic-looking data
        double latitude = 34.0522 + (random.nextDouble() - 0.5); // Around Los Angeles
        double longitude = -118.2437 + (random.nextDouble() - 0.5);
        double speed = 40 + random.nextDouble() * 40; // Speed between 40 and 80
        double fuelLevel = 10 + random.nextDouble() * 90; // Fuel between 10% and 100%

        // Create the telemetry object
        VehicleTelemetry telemetry = VehicleTelemetry.create(vehicleId, latitude, longitude, speed, fuelLevel);

        // Use our service to send the data
        producerService.sendTelemetry(telemetry);
    }
}
