package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) ->
                (new RegisterHandler()).handleRequest(req, res));




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
