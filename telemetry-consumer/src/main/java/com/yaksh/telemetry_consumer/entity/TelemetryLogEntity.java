package com.yaksh.telemetry_consumer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Table(name = "telemetry_logs")
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "vehicle_id", nullable = false)
  private String vehicleId;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  private double speed;

  @Column(name = "fuel_level")
  private double fuelLevel;

  @Column(nullable = false)
  private Instant timestamp;

}