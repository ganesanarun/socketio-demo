package com.corundumstudio.socketio.demo;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;

public class Sender implements ISender {
    private SocketIOServer server;

    public Sender(SocketIOServer _server) {
        server = _server;
    }

    public void broadcastMessage(ChatObject data) {
        System.out.println(data.getMessage());
        // broadcast messages to all clients
        server.getBroadcastOperations().sendEvent("chatevent", data);
    }
}