package ui;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.ListGamesResult;
import results.UserResult;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerFacade {
    private String authToken = "";
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

        } catch (IOException | URISyntaxException e) {
            System.out.println("User Already Exists");
            return "";
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

        } catch (IOException | URISyntaxException e) {
            System.out.println("User Already Exists");
            return "";
        }
    }

    public String listGames(String authToken){
        try {
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String listGames = clientCommunicator.getCommunicator("/game", authToken);
            return listGames;

        } catch (IOException e) {
            System.out.println("Unauthorized: AuthToken not in Database");
            return "";
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
