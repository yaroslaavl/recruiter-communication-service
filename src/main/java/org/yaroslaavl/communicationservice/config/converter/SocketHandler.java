package org.yaroslaavl.communicationservice.config.converter;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketHandler {

    private final SocketIOServer server;

    private final Map<String, String> clientToRoom = new ConcurrentHashMap<>();
    private final Map<String, List<String>> roomToClients = new ConcurrentHashMap<>();

    public SocketHandler(SocketIOServer server) {
        this.server = server;
        server.addListeners(this);
        server.start();
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        String room = clientToRoom.remove(clientId);

        if (room != null) {
            List<String> clients = roomToClients.get(room);
            if (clients != null) {
                clients.remove(clientId);
                if (clients.isEmpty()) {
                    roomToClients.remove(room);
                }
            }

            server.getRoomOperations(room).sendEvent("userDisconnected", clientId);
            log.info("Client {} disconnected from room {}", clientId, room);
        }
    }

    @OnEvent("joinRoom")
    public void onJoinRoom(SocketIOClient client, String roomId) {
        String sid = client.getSessionId().toString();

        List<String> clients = roomToClients.computeIfAbsent(
                roomId,
                r -> Collections.synchronizedList(new ArrayList<>())
        );

        if (clients.size() >= 2) {
            client.sendEvent("full", roomId);
            return;
        }

        client.joinRoom(roomId);
        clients.add(sid);
        clientToRoom.put(sid, roomId);

        if (clients.size() == 1) {
            client.sendEvent("created", roomId);
        } else if (clients.size() == 2) {
            client.sendEvent("joined", roomId);
            client.sendEvent("setCaller", clients.get(0));
            server.getRoomOperations(roomId).sendEvent("ready", roomId);
        }
    }

    @OnEvent("candidate")
    public void onCandidate(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        server.getRoomOperations(room).sendEvent("candidate", payload);
    }

    @OnEvent("offer")
    public void onOffer(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        server.getRoomOperations(room).sendEvent("offer", payload);
    }

    @OnEvent("answer")
    public void onAnswer(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        server.getRoomOperations(room).sendEvent("answer", payload);
    }

    @OnEvent("leaveRoom")
    public void onLeaveRoom(SocketIOClient client, String roomId) {
        client.leaveRoom(roomId);
        log.info("Client {} left room {}", client.getSessionId(), roomId);
    }
}
