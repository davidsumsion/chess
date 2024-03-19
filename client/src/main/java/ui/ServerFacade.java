package ui;

import results.ListGamesResult;

import java.io.IOException;

public class ServerFacade {
    public ServerFacade() {
    }

    public String listGames(String authToken){
        try {
            ClientCommunicator clientCommunicator = new ClientCommunicator();
            String listGames = clientCommunicator.getComunicator("/game", authToken);
            return listGames;

        } catch (IOException e) {
            System.out.println("ERROR with listGames in ServerFacade");
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
