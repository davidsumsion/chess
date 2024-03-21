package ui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import models.GameData;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.MessageOnlyResult;
import results.UserResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
//import spark.*;

public class ServerFacade {
    private String port = "8080";
    private static String authToken = "";
    public ServerFacade() {
//        getPort();
//        port = Spark.run(0);
    }

    public ServerFacade(String port) {
        this.port = port;
    }

//    public void getPort(){
//        try {
//            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
//                if (propStream == null) throw new Exception("Unable to load db.properties");
//                Properties props = new Properties();
//                props.load(propStream);
//
//                this.port = String.valueOf(Integer.parseInt(props.getProperty("db.port")));
//            }
//        } catch (Exception ex) {
//            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
//        }
//    }

//    public ServerFacade(String port) {
//        this.port = port;
//    }

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
            String jsonString = gson.toJson(new JoinGameRequest(Integer.parseInt(gameID), playerColor));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String urlString = "http://localhost:" + port + "/game";
            MessageOnlyResult messageOnlyResult = clientCommunicator.joinPlayer(urlString, jsonString, this.authToken);
            if (messageOnlyResult.getMessage().equals("CORRECT")){
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
        }
    }
}


//        Spark.post("/user", (req, res) -> (new RegisterHandler()).handle(req, res));
//        Spark.post("/session", (req, res) -> (new LoginHandler()).handle(req, res));
//        Spark.delete("/session", (req, res) ->(new LogoutHandler()).handle(req,res));
//        Spark.get("/game", (req, res) -> (new ListGamesHandler()).handle(req, res));
//        Spark.post("/game", (req, res) -> (new CreateGameHandler()).handle(req, res));
//        Spark.put("/game", (req, res) -> (new JoinGameHandler()).handle(req, res));
//        Spark.delete("/db", (req, res) -> (new DeleteAllHandler()).handle(req, res));
