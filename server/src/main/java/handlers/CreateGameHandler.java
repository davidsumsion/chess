package handlers;

import com.google.gson.Gson;
import services.UnauthorizedException;
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
        createGameRequest.setAuthToken(request.headers("Authorization"));
        CreateGameService service = new CreateGameService();
        CreateGameResult result = null;
        try {
            result = service.createGame(createGameRequest);
            response.status(200);
        } catch (UnauthorizedException e) {
            response.status(401);
            result = new CreateGameResult(e.getMessage());
        }
        return gson.toJson(result);
    }
}
