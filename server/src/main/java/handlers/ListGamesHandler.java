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
//        AuthTokenRequest authTokenRequest = gson.fromJson(request.body(), AuthTokenRequest.class);
        AuthTokenRequest authTokenRequest = new AuthTokenRequest(request.headers("Authorization"));
//        authTokenRequest.setAuthToken(request.headers("Authorization"));
        ListGamesService service = new ListGamesService();
        ListGamesResult result = service.listGames(authTokenRequest);

        if (result.getMessage() == null) {
            response.status(200);
        } else {
            response.status(401);
        }

        Gson gson = new Gson();
        return gson.toJson(result);
    }
}
