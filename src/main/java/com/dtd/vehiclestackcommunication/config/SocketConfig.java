package com.dtd.vehiclestackcommunication.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@Slf4j
public class SocketConfig {

    @Value("${socket.io.host}")
    private String host;

    @Value("${socket.io.port}")
    private Integer port;

    @Bean
    SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        return new SocketIOServer(config);
    }

}
