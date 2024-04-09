package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.MessageOnlyResult;
import results.UserResult;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class ServerFacade {
    private String port = "8080";
    private static String authToken = "";

    private String username;

    private WSCommunicator ws;
    public ServerFacade(ServerMessageObserver receiveMessage) {
        // take in port if needed, this(port)
        try {
            this.ws = new WSCommunicator(receiveMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ServerFacade(String port) {
        //only for phase 5 tests, no websocket tests
        //
        this.port = port;
    }


    public void leave(Integer gameID){
        Gson gson = new Gson();
        Leave leave = new Leave(authToken, gameID);
        try {
            ws.send(gson.toJson(leave));
        } catch (Exception e) {
            System.out.print("ERROR LEAVING");
        }
    }

    public void makeMove(Integer gameID, ChessMove move){
        Gson gson = new Gson();
        MakeMove makeMove = new MakeMove(authToken, gameID, move);
        try {
            ws.send(gson.toJson(makeMove));
        } catch (Exception e) {
            System.out.println("Error sending makeMove message");
        }
    }

    public String register(String username, String password, String email){
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new RegisterRequest(username, password, email));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/user";
            UserResult res = clientCommunicator.postRegisterCommunicator(urlString, jsonString);
            this.authToken = res.getAuthToken();
            this.username = res.getUsername();
            return this.username;
        } catch (NullPointerException e) {
            return "User Already Exists";
        } catch (IOException | URISyntaxException e) {
            return "Start the Server";
        }
    }

    public String login(String username, String password) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new LoginRequest(username, password));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/session";
            UserResult res = clientCommunicator.postLoginCommunicator(urlString, jsonString);
            this.authToken = res.getAuthToken();
            this.username = res.getUsername();
            return this.username;
        } catch (IOException | URISyntaxException e) {
            return "Start the Server";
        } catch (NullPointerException e) {
            return "Username or Password Incorrect";
        }
    }

    public String createGame(String gameName) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new CreateGameRequest(gameName));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/game";
            CreateGameResult res = clientCommunicator.postGameCommunicator(urlString, jsonString, this.authToken);
            if (res == null) { throw new NullPointerException(); }
            return res.getGameID().toString();
        } catch (NullPointerException e) {
            return "Game Already Exists or Unauthorized";
        } catch (IOException | URISyntaxException e) {
            return "Start the Server";
        }
    }

    public String listGames(){
        try {
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/game";
            ListGamesResult listGamesResult = clientCommunicator.getCommunicator(urlString, this.authToken);
            if (listGamesResult == null) { throw new NullPointerException(); }
            return listGamesResult.toString();
        } catch (NullPointerException | JsonSyntaxException e) {
            return "Unauthorized: AuthToken not in Database";
        } catch (IOException e) {
            return "Start the Server";
        }
    }

    public String logout(){
        try {
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/session";
            UserResult userResult = clientCommunicator.logoutDelete(urlString, this.authToken);
            if (userResult == null) { return "Unauthorized: AuthToken not in Database"; }
            this.authToken = "";
            return "";
        } catch (IOException | URISyntaxException e) {
            return "Start the Server";
        }
    }

    public String joinPlayer(String gameID, String playerColor){
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new JoinGameRequest(parseInt(gameID), playerColor));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/game";
            MessageOnlyResult messageOnlyResult = clientCommunicator.joinPlayer(urlString, jsonString, this.authToken);
            if (messageOnlyResult.getMessage().equals("CORRECT")){
                if (playerColor == null){
//                    JoinPlayer joinPlayer = new JoinPlayer(authToken, parseInt(gameID), null, username);
                    JoinObserver joinObserver = new JoinObserver(authToken, parseInt(gameID), username);
                    ws.send(gson.toJson(joinObserver));
                } else {
                    ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
                    if (!Objects.equals(playerColor, "WHITE")) {
                        teamColor = ChessGame.TeamColor.BLACK;
                    }
                    JoinPlayer joinPlayer = new JoinPlayer(authToken, parseInt(gameID), teamColor, this.username);
                    ws.send(gson.toJson(joinPlayer));
                }
                return "";
            } else if (messageOnlyResult.getMessage().equals("Error: Color already occupied")) {
                return "Error Color";
            } else if (messageOnlyResult.getMessage().equals("Error: Game not found in DB")) {
                return "Error Game";
            } else if (messageOnlyResult.getMessage().equals("Unauthorized")) {
                return "Unauthorized";
            } else { throw new IOException(); }
        } catch (IOException | URISyntaxException e) {
            return "Start the Server";
        } catch (NullPointerException e) {
            return "Unauthorized";
        } catch (Exception e) {
            // fix this for later to handle error
            return "Start the Server";
        }
    }

//    public String receiveLatestData(){
////        try {
////            ws.send("MESSAGE");
////            return "";
////        } catch (Exception e) {
////            System.out.print("an ERROR OCCURRED IN GET LATEST GAME");
////            return "";
////        }
//    }
}
