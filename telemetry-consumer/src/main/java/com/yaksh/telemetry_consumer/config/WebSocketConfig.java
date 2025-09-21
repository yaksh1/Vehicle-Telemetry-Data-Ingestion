package com.yaksh.telemetry_consumer.config;

import com.yaksh.telemetry_consumer.handler.TelemetryWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TelemetryWebSocketHandler telemetryWebSocketHandler;

    public WebSocketConfig(TelemetryWebSocketHandler telemetryWebSocketHandler) {
        this.telemetryWebSocketHandler = telemetryWebSocketHandler;
    }

    /**
     * Registers our custom WebSocket handler.
     * This tells Spring to route any WebSocket connection attempts at "/telemetry"
     * to our TelemetryWebSocketHandler.
     * .setAllowedOrigins("*") allows connections from any domain, which is fine for development.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(telemetryWebSocketHandler, "/telemetry")
                .setAllowedOrigins("*");
    }
}
