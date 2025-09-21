package com.yaksh.telemetry_producer.service;
import com.yaksh.telemetry_producer.model.VehicleTelemetry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

// Add this annotation if the class isn't already a component/service
@Component
public class TelemetrySimulator {

    private final TelemetryProducerService producerService;
    private final Random random = new Random();

    // A thread-safe map to hold the state of each vehicle simulator.
    // The key is the vehicleId.
    private final Map<String, VehicleState> fleetState = new ConcurrentHashMap<>();

    // A simple record to hold the current state and movement vector of a vehicle.
    private record VehicleState(
            String vehicleId,
            double lat,
            double lon,
            double latIncrement, // How much to move north/south each step
            double lonIncrement  // How much to move east/west each step
    ) {}

    public TelemetrySimulator(TelemetryProducerService producerService) {
        this.producerService = producerService;
        initializeFleet();
    }

    /**
     * Sets up the initial state of our simulated fleet with diverse starting points
     * and routes around Stony Brook.
     */
    private void initializeFleet() {
        // Vehicle 1: Starts at SBU and moves Northeast
        fleetState.put("SBU-Bus-01", new VehicleState("SBU-Bus-01", 40.9145, -73.1234, 0.00010, 0.00015));

        // Vehicle 2: Starts at the LIRR station and moves West
        fleetState.put("LIRR-Express-44", new VehicleState("LIRR-Express-44", 40.9023, -73.1300, 0, -0.00020));

        // Vehicle 3: Starts on Nicolls Road and moves Southeast
        fleetState.put("Nicolls-Patrol-07", new VehicleState("Nicolls-Patrol-07", 40.9160, -73.1160, -0.00012, 0.00012));

        // You can easily add more vehicles here if you want.
    }

    /**
     * This method runs every second, updates the state of ALL vehicles in the fleet,
     * and sends their new telemetry data to Kafka.
     */
    @Scheduled(fixedRate = 1000)
    public void updateAndSendAllTelemetry() {
        // Iterate over each vehicle in our state map
        for (String vehicleId : fleetState.keySet()) {

            // 1. Get the vehicle's current state
            VehicleState currentState = fleetState.get(vehicleId);

            // 2. Calculate the new position
            double newLat = currentState.lat() + currentState.latIncrement();
            double newLon = currentState.lon() + currentState.lonIncrement();

            // (Optional) Add logic here to make vehicles "bounce" off boundaries 
            // or change direction to make the simulation run forever.

            // 3. Create the updated state object
            VehicleState newState = new VehicleState(
                    vehicleId,
                    newLat,
                    newLon,
                    currentState.latIncrement(),
                    currentState.lonIncrement()
            );

            // 4. Update the state in our map for the next iteration
            fleetState.put(vehicleId, newState);

            // 5. Simulate some dynamic data for speed and fuel
            double speed = 65 + (random.nextDouble() * 10 - 5); // Speed fluctuates around 65
            double fuelLevel = 80 - (random.nextDouble() * 2);  // Fuel slowly decreases

            // 6. Create the telemetry object to be sent
            VehicleTelemetry telemetry = VehicleTelemetry.create(
                    vehicleId,
                    newState.lat(),
                    newState.lon(),
                    speed,
                    fuelLevel
            );

            // 7. Send the data to Kafka
            producerService.sendTelemetry(telemetry);
        }
    }
}