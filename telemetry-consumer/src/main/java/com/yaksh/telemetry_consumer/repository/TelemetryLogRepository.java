package com.yaksh.telemetry_consumer.repository;

import com.yaksh.telemetry_consumer.entity.TelemetryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryLogRepository extends JpaRepository<TelemetryLogEntity, Long> {
}