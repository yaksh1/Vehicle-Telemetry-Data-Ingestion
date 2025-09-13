package com.yaksh.telemetry_consumer.config;

import com.yaksh.telemetry_consumer.model.VehicleTelemetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Creates a customized RedisTemplate bean.
     * Spring Boot provides a RedisTemplate by default, but it's configured to handle
     * raw bytes, which isn't very human-readable or easy to work with.
     *
     * This custom configuration tells Redis how to "serialize" our data:
     * - The Key (e.g., "vehicle:vehicle-1") will be a plain string.
     * - The Value (the VehicleTelemetry object) will be stored as a JSON string.
     *
     * @param connectionFactory Provided automatically by Spring Boot.
     * @return A fully configured RedisTemplate.
     */
    @Bean
    public RedisTemplate<String, VehicleTelemetry> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, VehicleTelemetry> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Configure the serializer for the key
        template.setKeySerializer(new StringRedisSerializer());

        // Configure the serializer for the value
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(VehicleTelemetry.class));

        // Also configure hash key and hash value serializers
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(VehicleTelemetry.class));

        template.afterPropertiesSet();
        return template;
    }
}
