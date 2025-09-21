package com.yaksh.telemetry_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelemetryConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemetryConsumerApplication.class, args);
	}

}
