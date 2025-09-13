package com.yaksh.telemetry_consumer.model;


/**
 * A Java Record to represent a single telemetry event from a vehicle.
 * THIS MUST BE AN EXACT COPY of the record in the producer project to ensure
 * successful deserialization from JSON. It's the data contract between our services.
 */
public record VehicleTelemetry(
        String vehicleId,
        double latitude,
        double longitude,
        double speed,
        double fuelLevel,
        long timestamp
) {
    // No business logic needed here. This is just a data carrier.
}
