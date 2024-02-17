package server;

import handlers.RegisterHandler;
import spark.*;
import handlers.*;

public class Server {

    public static void main(String[] args){
        Server server = new Server();
        server.run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new RegisterHandler()).handle(req, res));




//        Spark.get("/", (request, response) -> {
//            //
//        });

//        private static void createRoutes() {
//            Spark.get("/hello", (req, res) -> "Hello BYU!");
//        }

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
