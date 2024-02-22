package handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import requests.CreateGameRequest;
import results.CreateGameResult;
import services.CreateGameService;

public class CreateGameHandler {
    public CreateGameHandler(){};

    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        CreateGameRequest createGameRequest = gson.fromJson(request.body(), CreateGameRequest.class);
        createGameRequest.setAuthToken(request.headers("Authorize"));

        CreateGameService service = new CreateGameService();
        CreateGameResult result = service.createGame(createGameRequest);
        if (result.getMessage() == null) {
            response.status(200);
        } else if (result.getMessage().contains("Authorized")){
            response.status(403);
        } else {
            response.status(401);
        }
        return gson.toJson(result);
    }
}
