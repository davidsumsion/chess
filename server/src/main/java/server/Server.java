package server;

import WSLogic.JoinGame;
import WSLogic.PlayerHolder;
import WSLogic.WSException;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySqlGameDataDA;
import models.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import requests.JoinGameRequest;
import services.GameServices.JoinGameService;
import spark.*;
import handlers.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebSocket
public class Server {
//    Map<Integer, PlayerHolder> sessionTracker = new HashMap<>();
    Map<Integer, ArrayList<Session>> sessionPlayerMap = new HashMap<>();
    Map<Integer, ArrayList<Session>> sessionObserverMap = new HashMap<>();
    ArrayList<Integer> resignedGames = new ArrayList<>();

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
                    System.out.println("JOIN PLAYER");
                    joinPlayerErrors(joinPlayer.getGameID(), joinPlayer.getPlayerColor(), joinPlayer.getAuthString(), gson, joinPlayer.getUsername());
                    addPlayerToMap(session, joinPlayer.getGameID(), gson);
                    String messageToObserver = "User: " + joinPlayer.getUsername() + " Joined the Game!";
                    notifyAllObservers(joinPlayer.getGameID(), gson, messageToObserver);
                    notifyPlayersJoinPlayer(joinPlayer.getGameID(), joinPlayer.getPlayerColor(), gson, session, joinPlayer.getUsername());
                } catch (WSException e){
                    System.out.println("Made it");
                    Error error = new Error(e.getMessage());
                    session.getRemote().sendString(gson.toJson(error));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            case JOIN_OBSERVER -> {
                JoinObserver joinObserver = gson.fromJson(message, JoinObserver.class);
                try {
                    joinObserverErrors(joinObserver.getGameID(), joinObserver.getAuthString(), gson, joinObserver.getUsername());

                    addObserverToMap(session, joinObserver.getGameID(), gson);
                    String messageObserverJoined = "User: " + joinObserver.getUsername() + " Joined the Game as an Observer!";
                    notifyAllObserversNotCurrent(joinObserver.getGameID(), gson, messageObserverJoined, session);
                    notifyAllPlayers(joinObserver.getGameID(), gson, messageObserverJoined);
                    //send current board to session
                    JoinGame joinGame = new JoinGame(joinObserver.getGameID(), null);
                    String jsonServerMessage2 = gson.toJson(joinGame.loadGame());
                    session.getRemote().sendString(jsonServerMessage2);
                } catch (WSException e){
                    System.out.println("Made it");
                    Error error = new Error(e.getMessage());
                    session.getRemote().sendString(gson.toJson(error));
                }catch (Exception e) {
                    System.out.println("Error in Join Observer: " + e.getMessage());
                }
            }
            case MAKE_MOVE -> {
                try {
//                    System.out.println("MAKE MOVE");
                    //if is not turn throw error
                    MakeMove makeMove = gson.fromJson(message, MakeMove.class);
                    if (resignedGames.contains(makeMove.getGameID())){
                        throw new WSException("The other player has resigned");
                    }
                    JoinGame joinGame = new JoinGame(makeMove.getGameID(), null);
                    GameData gameData = joinGame.getGame();
                    String usernameFromAuth = joinGame.findUsername(makeMove.getAuthString());
                    String chessGameJson = gameData.getChessGame();
                    ChessGame chessGame = gson.fromJson(chessGameJson, ChessGame.class);
                    ChessGame.TeamColor teamColor = chessGame.getTeamTurn();
                    if (teamColor == ChessGame.TeamColor.BLACK && !gameData.getBlackUsername().equals(usernameFromAuth)){
                        throw new WSException("NOT YOUR TURN");
                    }
                    if (teamColor == ChessGame.TeamColor.WHITE && !gameData.getWhiteUsername().equals(usernameFromAuth)){
                        throw new WSException("NOT YOUR TURN");
                    }

                    joinGame.makeMove(makeMove.getMove());
                    for (Session sesh: sessionPlayerMap.get(makeMove.getGameID())){
                        String jsonServerMessage2 = gson.toJson(joinGame.loadGame());
                        sesh.getRemote().sendString(jsonServerMessage2);
                        if (sesh != session){
                            sesh.getRemote().sendString(gson.toJson(new Notification("The other player moved")));
                        }
                    }
                    for (Session sesh: sessionObserverMap.get(makeMove.getGameID())){
                        String jsonServerMessage3 = gson.toJson((joinGame.loadGame()));
                        sesh.getRemote().sendString(jsonServerMessage3);
                        sesh.getRemote().sendString(gson.toJson(new Notification("Someone moved")));
                    }
                } catch (WSException e) {
                    session.getRemote().sendString(gson.toJson(new Error(e.getMessage())));
                }
            }
            case LEAVE -> {
                try {
                    System.out.println("LEAVE");
                    String notificationMessage = "USER: " + " LEFT THE GAME";
                    Leave leave = gson.fromJson(message, Leave.class);
                    Boolean indicator = false;
                    for (Session sesh: sessionPlayerMap.get(leave.getGameID())) {
                        Notification notification = new Notification(notificationMessage);
                        sesh.getRemote().sendString(gson.toJson(notification));
                        if (session == sesh){
                            indicator = true;
                        }
                    }
                    if (indicator) {
                        sessionPlayerMap.get(leave.getGameID()).remove(session);
                    }
                    Boolean indicator2 = false;
                    for (Session sesh: sessionObserverMap.get(leave.getGameID())){
                        Notification notification = new Notification(notificationMessage);
                        sesh.getRemote().sendString(gson.toJson(notification));
                        if (session == sesh){
                            indicator2 = true;
                        }
                    }
                    if (indicator2) {
                        sessionObserverMap.get(leave.getGameID()).remove(session);
                    }
                } catch (Exception e) {
                    System.out.println("error with leave case");
                }
            }
            case RESIGN -> {
                try {
                    System.out.print("RESIGN");
                    Resign resign = gson.fromJson(message, Resign.class);

                    if (resignedGames != null && resignedGames.contains(resign.getGameID())){
                        System.out.println("NO MESSAGE SEND resignedGames");
                        throw new WSException("cannot resign twice");
                    }

                    if (sessionObserverMap.get(resign.getGameID()).contains(session)){
                        System.out.println("NO MESSAGE SEND sessionObserverMap");
                        throw new WSException("Observer cannot resign");
                    }

                    resignedGames.add(resign.getGameID());

                    for (Session sesh: sessionPlayerMap.get(resign.getGameID())) {
                        String notificationMessage = "USER: " + " LEFT THE GAME";
                        Notification notification = new Notification(notificationMessage);
                        sesh.getRemote().sendString(gson.toJson(notification));
                    }
                    for (Session sesh: sessionObserverMap.get(resign.getGameID())){
                        String notificationMessage = "USER: " + " LEFT THE GAME";
                        Notification notification = new Notification(notificationMessage);
                        sesh.getRemote().sendString(gson.toJson(notification));
                    }


                    if (resignedGames != null && resignedGames.contains((resign.getGameID()))){
                        System.out.println("NO MESSAGE SEND resignedGames Bottom");
                        throw new WSException("Already Resigned");
                    }
                } catch (Exception e) {
                    Error error = new Error(e.getMessage());
                    session.getRemote().sendString(gson.toJson(error));
                }

            }
        }
    }

    public void joinObserverErrors(Integer gameID, String authToken, Gson gson, String username) throws WSException{
        JoinGameService joinGameService = new JoinGameService();
        try (Connection connection = DatabaseManager.getConnection()){
            GameData gameDataDB = joinGameService.findGame(connection, gameID);
            String myUsername = "";
            JoinGame joinGame = new JoinGame();
            myUsername = joinGame.findUsername(authToken);
            // join observer bad authtoken
            if (authToken == null || myUsername == null ){
                throw new WSException("unauthorized authtoken");
            }
            if (gameDataDB == null) {
                throw new WSException("Game DNE");
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error accessing data");
        }
    }
    public void joinPlayerErrors(Integer gameID, ChessGame.TeamColor playerColor, String authToken, Gson gson, String username) throws WSException{
        JoinGameService joinGameService = new JoinGameService();
        try (Connection connection = DatabaseManager.getConnection()){
            GameData gameDataDB = joinGameService.findGame(connection, gameID);
            String useUsername = username;
            if (username == null) {
                JoinGame joinGame = new JoinGame();
                useUsername = joinGame.findUsername(authToken);
            }
            if (gameDataDB == null) {
                throw new WSException("Game DNE");
            }
            // if game is empty
            if (gameDataDB.getChessGame() == null) {
                throw new WSException("Game is Empty");
            }
//            //if player joins occupied color
            if (playerColor == ChessGame.TeamColor.BLACK){
                if (gameDataDB.getBlackUsername() == null){
                    //add username to black username in DB
                    throw new WSException("USERNAME IS NULL");
                } else if (!Objects.equals(gameDataDB.getBlackUsername(), useUsername)){
                    System.out.println("WHITE:");
                    System.out.println(gameDataDB.getBlackUsername() != useUsername);
                    throw new WSException("Black Already Occupied");
                }
            } else if (playerColor == ChessGame.TeamColor.WHITE){
                if (gameDataDB.getWhiteUsername() == null){
                    // add username to white username in DB
                    throw new WSException("USERNAME IS NULL");
                }
                if (!Objects.equals(gameDataDB.getWhiteUsername(), useUsername)) {
                    System.out.println("WHITE:");
                    System.out.println("DBUSER: " + gameDataDB.getWhiteUsername() + "\nWSUSER: " + useUsername);
                    System.out.println(gameDataDB.getWhiteUsername() != useUsername);
                    throw new WSException("White Already Occupied");
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error accessing data");
        }
        // if player tries to join a game with a bad authtoken
            //get the game, bad authtoken result
        //
    }

    public synchronized void notifyAllObservers(Integer gameID, Gson gson, String message) {
        if (!sessionObserverMap.containsKey(gameID)) {
            sessionObserverMap.put(gameID, new ArrayList<Session>());
        }
        for (Session sesh: sessionObserverMap.get(gameID)){
            try {
                Notification notificationMessage = new Notification(message);
                sesh.getRemote().sendString(gson.toJson(notificationMessage));
            } catch (Exception e) {
                System.out.println("Error with All Observer Notification");
            }
        }
    }

//    public void sendErrorMessage(Error error, Integer gameID){
//
//    }

    public synchronized void notifyAllObserversNotCurrent(Integer gameID, Gson gson, String message, Session session) {
        if (!sessionObserverMap.containsKey(gameID)) {
            sessionObserverMap.put(gameID, new ArrayList<Session>());
        }
        for (Session sesh: sessionObserverMap.get(gameID)){
            try {
                if (!sesh.equals(session)){
                    Notification notificationMessage = new Notification(message);
                    sesh.getRemote().sendString(gson.toJson(notificationMessage));
                }
            } catch (Exception e) {
                System.out.println("Error with All Observer Notification");
            }
        }
    }

    public  synchronized void notifyAllPlayers(Integer gameID, Gson gson, String message){
        if (!sessionPlayerMap.containsKey(gameID)) {
            sessionPlayerMap.put(gameID, new ArrayList<Session>());
        }
        for (Session sesh: sessionPlayerMap.get(gameID)){
            try {
                Notification notificationMessage = new Notification(message);
                sesh.getRemote().sendString(gson.toJson(notificationMessage));
            } catch (Exception e) {
                System.out.println("Error with All Observer Notification");
            }
        }
    }

    public void addPlayerToMap(Session session, Integer gameID, Gson gson){
        if (!sessionPlayerMap.containsKey(gameID)) {
            sessionPlayerMap.put(gameID, new ArrayList<Session>());
        }
        ArrayList<Session> newSessions = sessionPlayerMap.get(gameID);
        newSessions.add(session);
        sessionPlayerMap.put(gameID, newSessions);
    }

    public void addObserverToMap(Session session, Integer gameID, Gson gson){
        if (!sessionObserverMap.containsKey(gameID)) {
            sessionObserverMap.put(gameID, new ArrayList<Session>());
        }
        ArrayList<Session> newSessions = sessionObserverMap.get(gameID);
        newSessions.add(session);
        sessionObserverMap.put(gameID, newSessions);
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
        System.out.println("WEBSOCKET CLOSED");
        for (Integer gameID : sessionPlayerMap.keySet()){
            ArrayList<Session> array = sessionPlayerMap.get(gameID);
            Boolean indicator = false;
            if (array != null){
                for (Session sesh: array){
                    if (sesh == session){
                        indicator = true;
                    }
                }
            }
            if (indicator){
                array.remove(session);
                sessionPlayerMap.put(gameID, array);
            }
        }
        for (Integer gameID : sessionObserverMap.keySet()){
            ArrayList<Session> array = sessionObserverMap.get(gameID);
            Boolean indicator = false;
            if (array != null){
                for (Session sesh: array){
                    if (sesh == session){
                        indicator = true;
                    }
                }
            }
            if (indicator){
                array.remove(session);
                sessionObserverMap.put(gameID, array);
            }
        }
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
