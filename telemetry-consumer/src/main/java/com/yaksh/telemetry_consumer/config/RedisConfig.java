// com/yaksh/telemetry_consumer/config/RedisConfig.java (Updated)

package com.yaksh.telemetry_consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Creates a customized RedisTemplate bean optimized for working with Hashes.
     *
     * This custom configuration sets serializers for:
     * - Key (e.g., "vehicle:vehicle-1"): Stored as a plain string.
     * - Hash Key (e.g., "speed", "fuelLevel"): Stored as a plain string.
     * - Hash Value (e.g., 75.5, 90.2): Stored as JSON, allowing for different
     * data types (numbers, strings, etc.) within the same hash.
     *
     * @param connectionFactory Provided automatically by Spring Boot.
     * @return A fully configured RedisTemplate.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use StringSerializer for top-level keys and hash keys
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Use a generic JSON serializer for hash values to handle diverse types
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}