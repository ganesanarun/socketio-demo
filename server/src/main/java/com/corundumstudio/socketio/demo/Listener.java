package com.corundumstudio.socketio.demo;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;

public class Listener implements DataListener<ChatObject> {
    private ISender sender;

    public Listener(ISender _sender) {
        sender = _sender;
    }

    @Override
    public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
        System.out.println(data.getMessage());
        // broadcast messages to all clients
        sender.broadcastMessage(data);
    }
}