package ui;

import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
    void notify(String serverMessage);
}
