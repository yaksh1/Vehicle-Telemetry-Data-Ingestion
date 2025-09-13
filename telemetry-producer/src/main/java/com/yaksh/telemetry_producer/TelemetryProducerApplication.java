package com.yaksh.telemetry_producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelemetryProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemetryProducerApplication.class, args);
	}

}
