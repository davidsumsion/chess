package handlers;

import com.google.gson.Gson;
import results.UserResult;
import spark.Request;
import spark.Response;
import requests.AuthTokenRequest;
import services.LogoutService;

public class LogoutHandler {
    public LogoutHandler(){};

    public Object handle(Request request, Response response) throws Exception{
        Gson gson = new Gson();
        AuthTokenRequest authTokenRequest = (AuthTokenRequest)gson.fromJson(request.body(), AuthTokenRequest.class);
        LogoutService service = new LogoutService();
        UserResult result = service.logout(authTokenRequest);
        response.status(200);
        return gson.toJson(result);
    }
}
