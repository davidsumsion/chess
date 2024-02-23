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
        AuthTokenRequest authTokenRequest = new AuthTokenRequest(request.headers("Authorization"));
        LogoutService service = new LogoutService();
        UserResult result = service.logout(authTokenRequest);
        if (result.getMessage() == null){
            response.status(200);
        } else {
            response.status(401);
        }
        return gson.toJson(result);
    }
}
