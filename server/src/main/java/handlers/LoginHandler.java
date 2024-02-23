package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import results.UserResult;
import services.LoginService;
import services.UnauthorizedException;
import spark.Request;
import spark.Response;

public class LoginHandler {
    public LoginHandler(){};

    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        LoginService service = new LoginService();
        UserResult result = null;
        try {
            result = service.login(loginRequest);
            response.status(200);
        } catch (UnauthorizedException e){
            result = new UserResult(null, null);
            result.setMessage(e.getMessage());
            response.status(401);
        }
        return gson.toJson(result);
    }
}
