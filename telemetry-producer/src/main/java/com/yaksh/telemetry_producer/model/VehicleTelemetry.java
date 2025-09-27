package com.yaksh.telemetry_producer.model;

import java.time.Instant;

/**
 * A Java Record to represent a single telemetry event from a vehicle.
 * Records are a modern, concise way to create immutable data-carrier classes.
 */
public record VehicleTelemetry(
        String vehicleId,
        double latitude,
        double longitude,
        double speed,
        double fuelLevel,
        Instant timestamp
) {
    // We can add a convenience factory method if we want
    public static VehicleTelemetry create(String vehicleId, double lat, double lon, double speed, double fuel) {
        return new VehicleTelemetry(vehicleId, lat, lon, speed, fuel, Instant.now());
    }
}
