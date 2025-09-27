package org.yaroslaavl.communicationservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {
    private String endpoint = "/ws";
    private String applicationPrefix = "/app";
    private String userPrefix = "/user";
    private String[] brokerTopics = {"/chatroom", "/queue"};
}