package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WSCommunicator extends Endpoint {
    public Session session;
    public WSCommunicator(ServerMessageObserver serverMessageObserver) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                serverMessageObserver.notify(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}