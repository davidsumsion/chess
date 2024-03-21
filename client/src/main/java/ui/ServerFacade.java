package ui;

import com.google.gson.Gson;
import models.GameData;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.MessageOnlyResult;
import results.UserResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ServerFacade {
    private static String authToken = "";
    public ServerFacade() {
    }

    public String register(String username, String password, String email){
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new RegisterRequest(username, password, email));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            UserResult res = clientCommunicator.postRegisterCommunicator("http://localhost:8080/user", jsonString);
            this.authToken = res.getAuthToken();
            return res.getUsername();

        } catch (IOException | NullPointerException | URISyntaxException e) {
            return "User Already Exists";
        }
    }

    public String login(String username, String password) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new LoginRequest(username, password));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            UserResult res = clientCommunicator.postLoginCommunicator("http://localhost:8080/session", jsonString);
            this.authToken = res.getAuthToken();
            return res.getUsername();

        } catch (IOException | URISyntaxException | NullPointerException e) {
            return "Username or Password Incorrect";
        }
    }

    public String createGame(String gameName) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new CreateGameRequest(gameName));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            CreateGameResult res = clientCommunicator.postGameCommunicator("http://localhost:8080/game", jsonString, this.authToken);
            return res.getGameID().toString();

        } catch (IOException | URISyntaxException e) {
            System.out.println("User Already Exists");
            return "";
        }
    }

    public String listGames(){
        try {
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            ListGamesResult listGamesResult = clientCommunicator.getCommunicator("http://localhost:8080/game", this.authToken);
            return listGamesResult.toString();
//             return listGames;

        } catch (IOException e) {
            System.out.println("Unauthorized: AuthToken not in Database");
            return "";
        }
    }

    public String logout(){
        try {
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            UserResult userResult = clientCommunicator.logoutDelete("http://localhost:8080/session", this.authToken);
            if (userResult == null) { return "Unauthorized: AuthToken not in Database"; }
            this.authToken = "";
            return "";
        } catch (IOException | URISyntaxException e) {
            return "Start the Server";
        }
    }

    public void joinGamePlayer(String gameID, String playerColor){
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(new JoinGameRequest(Integer.parseInt(gameID), playerColor));
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            MessageOnlyResult messageOnlyResult = clientCommunicator.joinPlayer("http://localhost:8080/game", jsonString, this.authToken);
        } catch (IOException e) {
            System.out.println("Unauthorized: AuthToken not in Database");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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
