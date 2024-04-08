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
            @Override
            public void onMessage(String message) {
                System.out.print("\n" + message + "\n");
                try {
                    serverMessageObserver.notify(message);
                } catch (Exception e) {
                    throw new RuntimeException("Message improperly received");
                }
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){
        System.out.print("WebSocket Opened");
    }


    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//    }
}