package com.yaksh.telemetry_producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    public static final String TELEMETRY_TOPIC = "vehicle-telemetry";

    /**
     * This Spring Bean defines the Kafka topic we want to send messages to.
     * When the application starts, Spring Boot will check if this topic exists
     * in the Kafka cluster. If it doesn't, it will be created automatically.
     *
     * @return a NewTopic object representing our desired topic.
     */
    @Bean
    public NewTopic telemetryTopic() {
        return TopicBuilder.name(TELEMETRY_TOPIC)
                .partitions(1)      // We can start with 1 partition for simplicity.
                .replicas(1)        // In a real cluster, you'd have more replicas for fault tolerance.
                .build();
    }
}
