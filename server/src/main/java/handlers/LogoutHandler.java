package handlers;

import com.google.gson.Gson;
import results.UserResult;
import services.Exceptions.BadRequestException;
import spark.Request;
import spark.Response;
import requests.AuthTokenRequest;
import services.UserServices.LogoutService;

public class LogoutHandler {
    public LogoutHandler(){};

    public Object handle(Request request, Response response) throws Exception{
        Gson gson = new Gson();
        AuthTokenRequest authTokenRequest = new AuthTokenRequest(request.headers("Authorization"));
        LogoutService service = new LogoutService();
        UserResult result = null;
        try {
            result = service.logout(authTokenRequest);
            response.status(200);
        } catch (BadRequestException e) {
            result = new UserResult(null, null);
            result.setMessage(e.getMessage());
            response.status(401);
        }
        return gson.toJson(result);
    }
}
