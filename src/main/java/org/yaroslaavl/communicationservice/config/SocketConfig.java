package org.yaroslaavl.communicationservice.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.Configuration;

@org.springframework.context.annotation.Configuration
public class SocketConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9092);
        config.setPingInterval(25000);
        config.setPingTimeout(60000);
        return new SocketIOServer(config);
    }
}
