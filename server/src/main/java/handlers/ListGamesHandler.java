package handlers;

import com.google.gson.Gson;
import requests.AuthTokenRequest;
import results.ListGamesResult;
import services.ListGamesService;
import services.Exceptions.UnauthorizedException;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public ListGamesHandler() {
    }
    public Object handle(Request request, Response response) {
        AuthTokenRequest authTokenRequest = new AuthTokenRequest(request.headers("Authorization"));
        ListGamesService service = new ListGamesService();
        ListGamesResult result = null;
        try {
            result = service.listGames(authTokenRequest);
            response.status(200);
        } catch (UnauthorizedException e) {
            response.status(401);
            result = new ListGamesResult(e.getMessage());
        }
        Gson gson = new Gson();
        return gson.toJson(result);
    }
}
