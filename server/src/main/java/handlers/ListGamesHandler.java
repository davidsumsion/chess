package handlers;

import com.google.gson.Gson;
import requests.AuthTokenRequest;
import results.ListGamesResult;
import results.UserResult;
import services.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public ListGamesHandler() {
    }
    public Object handle(Request request, Response response) throws Exception{
        Gson gson = new Gson();
        AuthTokenRequest authTokenRequest = gson.fromJson(request.body(), AuthTokenRequest.class);
        ListGamesService service = new ListGamesService();
        ListGamesResult result = service.listGames(authTokenRequest);
        response.status(200);
        return gson.toJson(result);
    }
}
