package com.dtd.vehiclestackcommunication.commandlinerunner;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SocketIoServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;
    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    public SocketIoServerCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        if (Objects.isNull(profile) || !profile.equals("test")) {
            server.start();
        }
    }
}
