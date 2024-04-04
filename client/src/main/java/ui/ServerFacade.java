package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import models.GameData;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.MessageOnlyResult;
import results.UserResult;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static java.lang.Integer.parseInt;
//import spark.*;

public class ServerFacade {
    private String port = "8080";
    private static String authToken = "";

    private WSCommunicator ws;
    public ServerFacade() {
        try {
            this.ws = new WSCommunicator(this::receiveMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ServerFacade(String port) {
        this();
        this.port = port;
    }

    private void receiveMessage(String message) {
        Gson gson = new Gson();
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        System.out.println(serverMessage);

        switch (serverMessage.getServerMessageType()){
            case LOAD_GAME -> {
                System.out.print("LOAD GAME\n");
                LoadGame loadGame = gson.fromJson(message, LoadGame.class);
                Client.drawBoard(loadGame.getGame());

            }
            case ERROR -> System.out.print("ERROR");
            case NOTIFICATION -> System.out.print("NOTIFICATION");
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
            return res.getUsername();

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
            return res.getUsername();

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

    public String joinGamePlayer(String gameID, String playerColor){
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new JoinGameRequest(parseInt(gameID), playerColor));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/game";
            MessageOnlyResult messageOnlyResult = clientCommunicator.joinPlayer(urlString, jsonString, this.authToken);
            if (messageOnlyResult.getMessage().equals("CORRECT")){
                ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
                if (!Objects.equals(playerColor, "WHITE")) {
                    teamColor = ChessGame.TeamColor.BLACK;
                }
                JoinPlayer joinPlayer = new JoinPlayer(authToken, parseInt(gameID), teamColor);
                ws.send(gson.toJson(joinPlayer));
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
