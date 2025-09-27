package org.yaroslaavl.communicationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.yaroslaavl.communicationservice.config.properties.RabbitMqProperties;
import org.yaroslaavl.communicationservice.config.properties.WebSocketProperties;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RabbitMqProperties rabbitMqProperties;
    private final WebSocketProperties webSocketProperties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketProperties.getEndpoint()).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(webSocketProperties.getApplicationPrefix());
        registry.setUserDestinationPrefix(webSocketProperties.getUserPrefix());
        registry.enableStompBrokerRelay(webSocketProperties.getBrokerTopics())
                .setRelayHost(rabbitMqProperties.getHost())
                .setClientLogin(rabbitMqProperties.getUsername())
                .setClientPasscode(rabbitMqProperties.getPassword())
                .setSystemLogin(rabbitMqProperties.getUsername())
                .setSystemPasscode(rabbitMqProperties.getPassword())
                .setRelayPort(rabbitMqProperties.getPort())
                .setVirtualHost(rabbitMqProperties.getVirtualHost());
    }
}
