package server;

import WSLogic.JoinGame;
import WSLogic.PlayerHolder;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;
import handlers.*;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.UserGameCommand;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class Server {
    Map<Integer, PlayerHolder> sessionTracker = new HashMap<>();

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
//        session.getRemote().sendString("WebSocket response: " + message);
        Gson gson = new Gson();
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()){
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayer = gson.fromJson(message, JoinPlayer.class);
                //add player to session
                PlayerHolder playerHolder = null;
                if (sessionTracker.containsKey(joinPlayer.getGameID())) { playerHolder = sessionTracker.get(joinPlayer.getGameID()); }
                else { playerHolder = new PlayerHolder(); }
                playerHolder.addPlayer(session);
                sessionTracker.put(joinPlayer.getGameID(), playerHolder);

                //notify other players in game
                for (Session sesh: sessionTracker.get(joinPlayer.getGameID()).getPlayers()){
                    if (sesh.getRemote() == null) {
                        System.out.print("NULL");
                    }
                    if (!sesh.equals(session)){
                        String notificationMessage = "User: " + " Joined the game as " + joinPlayer.getPlayerColor();
                        Notification notification = new Notification(notificationMessage);
                        try {
                            sesh.getRemote().sendString(gson.toJson(notification));
                        } catch (Exception e) {
                            System.out.print("ERRRRROR");
                        }
                    }
                }
                //send current board to session
                JoinGame joinGame = new JoinGame(joinPlayer.getGameID(), joinPlayer.getPlayerColor());
                String jsonServerMessage = gson.toJson(joinGame.loadGame());
                session.getRemote().sendString(jsonServerMessage);
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
                            sesh.getRemote().sendString(gson.toJson(notification));
                        } catch (Exception e) {
                            System.out.print("ERRRRROR");
                        }
                    }
                }
//                String notificationMessage = "TEST NOTIFICATION";
//                Notification notification = new Notification(notificationMessage);
//                String jsonServerMessage = gson.toJson(notification);
//                session.getRemote().sendString(jsonServerMessage);
                //send current board to session
                JoinGame joinGame = new JoinGame(joinObserver.getGameID(), null);
                String jsonServerMessage2 = gson.toJson(joinGame.loadGame());
                session.getRemote().sendString(jsonServerMessage2);
            }
            case MAKE_MOVE -> System.out.print("MAKE MOVE");
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
                    session.getRemote().sendString(jsonServerMessage);
                }
                for (Session sesh: observersList){
                    String notificationMessage = "USER: " + " LEFT THE GAME";
                    Notification notification = new Notification(notificationMessage);
                    String jsonServerMessage = gson.toJson(notification);
                    session.getRemote().sendString(jsonServerMessage);
                }
            }
            case RESIGN -> System.out.print("RESIGN");
        }

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
