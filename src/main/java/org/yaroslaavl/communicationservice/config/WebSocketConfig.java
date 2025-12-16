package org.yaroslaavl.communicationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.yaroslaavl.communicationservice.config.properties.RabbitMqProperties;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RabbitMqProperties rabbitMqProperties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "https://172.20.10.2:3000",
                        "http://localhost:3000"
                );
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic/video");
        registry.enableStompBrokerRelay("/topic/chat", "/queue")
                .setRelayHost(rabbitMqProperties.getHost())
                .setRelayPort(rabbitMqProperties.getStomp())
                .setClientLogin(rabbitMqProperties.getUsername())
                .setClientPasscode(rabbitMqProperties.getPassword())
                .setSystemLogin(rabbitMqProperties.getUsername())
                .setSystemPasscode(rabbitMqProperties.getPassword())
                .setVirtualHost(rabbitMqProperties.getVirtualHost());
    }
}
