package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;
import handlers.*;
import webSocketMessages.userCommands.UserGameCommand;

import javax.xml.crypto.Data;

@WebSocket
public class Server {
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
        session.getRemote().sendString("WebSocket response: " + message);
        Gson gson = new Gson();
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()){
            case JOIN_PLAYER -> System.out.print("JOIN PLAYER");
            case JOIN_OBSERVER -> System.out.print("JOIN OBSERVER");
            case MAKE_MOVE -> System.out.print("MAKE MOVE");
            case LEAVE -> System.out.print("LEAVE");
            case RESIGN -> System.out.print("RESIGN");
        }

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
