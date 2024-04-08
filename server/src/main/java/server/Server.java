package server;

import WSLogic.JoinGame;
import WSLogic.PlayerHolder;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import spark.*;
import handlers.*;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class Server {
    Map<Integer, PlayerHolder> sessionTracker = new HashMap<>();
    Map<Integer, ArrayList<Session>> sessionPlayerMap = new HashMap<>();
    Map<Integer, ArrayList<Session>> sessionObserverMap = new HashMap<>();


    public static void main(String[] args){
        Server server = new Server();
        server.run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println("Cannot Create DB or Tables");
        }

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", Server.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new RegisterHandler()).handle(req, res));
        Spark.post("/session", (req, res) -> (new LoginHandler()).handle(req, res));
        Spark.delete("/session", (req, res) ->(new LogoutHandler()).handle(req,res));
        Spark.get("/game", (req, res) -> (new ListGamesHandler()).handle(req, res));
        Spark.post("/game", (req, res) -> (new CreateGameHandler()).handle(req, res));
        Spark.put("/game", (req, res) -> (new JoinGameHandler()).handle(req, res));
        Spark.delete("/db", (req, res) -> (new DeleteAllHandler()).handle(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        Gson gson = new Gson();
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()){
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayer = gson.fromJson(message, JoinPlayer.class);
                try {
                    addPlayerToMap(session, joinPlayer.getGameID(), gson);
                    notifyAllObservers(joinPlayer.getGameID(), gson);
                    notifyPlayersJoinPlayer(joinPlayer.getGameID(), joinPlayer.getPlayerColor(), gson, session, joinPlayer.getUsername());
                } catch (Exception e) {
                    System.out.print("Error: " + e.getMessage());
                }
            }
            case JOIN_OBSERVER -> {
                System.out.print("JOIN OBSERVER");
                JoinObserver joinObserver = gson.fromJson(message, JoinObserver.class);
                //add observer to session
                PlayerHolder playerHolder = null;
                if (sessionTracker.containsKey(joinObserver.getGameID())) { playerHolder = sessionTracker.get(joinObserver.getGameID()); }
                else { playerHolder = new PlayerHolder(); }
                playerHolder.addObserver(session);
                sessionTracker.put(joinObserver.getGameID(), playerHolder);

                //notify other players in game
                for (Session sesh: sessionTracker.get(joinObserver.getGameID()).getPlayers()){
                    if (!sesh.equals(session)){
                        String notificationMessage = "User: " + " Joined the game to watch";
                        Notification notification = new Notification(notificationMessage);
                        try {
                            if (sesh.isOpen()) {
                                sesh.getRemote().sendString(gson.toJson(notification));
                            }
                        } catch (Exception e) {
                            System.out.print("ERRRRROR");
                        }
                    }
                }
                //send current board to session
                JoinGame joinGame = new JoinGame(joinObserver.getGameID(), null);
                String jsonServerMessage2 = gson.toJson(joinGame.loadGame());
                if (session.isOpen()) {
                    session.getRemote().sendString(jsonServerMessage2);
                }
            }
            case MAKE_MOVE -> {
                System.out.print("MAKE MOVE");
                MakeMove makeMove = gson.fromJson(message, MakeMove.class);
                JoinGame joinGame = new JoinGame(makeMove.getGameID(), null);
                joinGame.makeMove(makeMove.getMove());

            }
            case LEAVE -> {
                System.out.print("LEAVE");
                Leave leave = gson.fromJson(message, Leave.class);
                List<Session> playersList = sessionTracker.get(leave.getGameID()).getPlayers();
                playersList.remove(session);
                List<Session> observersList = sessionTracker.get(leave.getGameID()).getObservers();
                observersList.remove(session);
                PlayerHolder newPlayerHolder = new PlayerHolder();
                newPlayerHolder.setPlayers(playersList);
                newPlayerHolder.setObservers(observersList);
                sessionTracker.put(leave.getGameID(), newPlayerHolder);

                for (Session sesh: playersList){
                    String notificationMessage = "USER: " + " LEFT THE GAME";
                    Notification notification = new Notification(notificationMessage);
                    String jsonServerMessage = gson.toJson(notification);
                    if (session.isOpen()) {
                        session.getRemote().sendString(jsonServerMessage);
                    }
                }
                for (Session sesh: observersList){
                    String notificationMessage = "USER: " + " LEFT THE GAME";
                    Notification notification = new Notification(notificationMessage);
                    String jsonServerMessage = gson.toJson(notification);
                    if (session.isOpen()) {
                        session.getRemote().sendString(jsonServerMessage);
                    }
                }
            }
            case RESIGN -> {
                System.out.print("RESIGN");
                Leave leave = gson.fromJson(message, Leave.class);
                List<Session> playersList = sessionTracker.get(leave.getGameID()).getPlayers();
                playersList.remove(session);
                List<Session> observersList = sessionTracker.get(leave.getGameID()).getObservers();
                observersList.remove(session);
                PlayerHolder newPlayerHolder = new PlayerHolder();
                newPlayerHolder.setPlayers(playersList);
                newPlayerHolder.setObservers(observersList);
                sessionTracker.put(leave.getGameID(), newPlayerHolder);

                for (Session sesh: playersList){
                    String notificationMessage = "USER: " + " LEFT THE GAME";
                    Notification notification = new Notification(notificationMessage);
                    String jsonServerMessage = gson.toJson(notification);
                    if (session.isOpen()) {
                        session.getRemote().sendString(jsonServerMessage);
                    }
                }
                for (Session sesh: observersList){
                    String notificationMessage = "USER: " + " LEFT THE GAME";
                    Notification notification = new Notification(notificationMessage);
                    String jsonServerMessage = gson.toJson(notification);
                    if (session.isOpen()) {
                        session.getRemote().sendString(jsonServerMessage);
                    }
                }
            }
        }
    }

    public synchronized void notifyAllObservers(Integer gameID, Gson gson) {
        if (!sessionObserverMap.containsKey(gameID)) {
            sessionObserverMap.put(gameID, new ArrayList<Session>());
        }
        for (Session sesh: sessionObserverMap.get(gameID)){
            try {
                Notification notificationMessage = new Notification("User X Joined the Game!");
                sesh.getRemote().sendString(gson.toJson(notificationMessage));
            } catch (Exception e) {
                System.out.println("Error with All Observer Notification");
            }
        }
    }

    public synchronized void addPlayerToMap(Session session, Integer gameID, Gson gson){
        if (!sessionPlayerMap.containsKey(gameID)) {
            sessionPlayerMap.put(gameID, new ArrayList<Session>());
        }
        ArrayList<Session> newSessions = sessionPlayerMap.get(gameID);
        newSessions.add(session);
        sessionPlayerMap.put(gameID, newSessions);
    }

    public synchronized void sendGame(Integer gameID, ChessGame.TeamColor playerColor, Gson gson, Session session) throws IOException {
        JoinGame joinGame = new JoinGame(gameID, playerColor);
        String jsonServerMessage = gson.toJson(joinGame.loadGame());
        session.getRemote().sendString(jsonServerMessage);
    }

    public synchronized void notifyObserver(String message, Gson gson, Session session) throws IOException {
        Notification notificationMessage = new Notification(message);
        session.getRemote().sendString(gson.toJson(notificationMessage));
    }

    public synchronized void notifyPlayersJoinPlayer(Integer gameID, ChessGame.TeamColor playerColor, Gson gson, Session session, String username) throws IOException {
        for (Session sesh: sessionPlayerMap.get(gameID)){
            if (session == sesh)  {
                sendGame(gameID, playerColor, gson, session);
            } else {
                notifyObserver("User: " + username +  " Joined the Game", gson, sesh);
            }
        }
    }


    @OnWebSocketError
    public void onError(Throwable throwable) {
        System.out.println("Websocket error!");
        System.out.println(throwable.toString());
    }

    @OnWebSocketClose
    public void onClose(Session session, int var2, String var3) {
        System.out.print("WEBSOCKET CLOSED");
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        System.out.println("Websocket opened.");
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
