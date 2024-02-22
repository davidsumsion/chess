package handlers;

import com.google.gson.Gson;
import requests.LoginRequest;
import results.UserResult;
import services.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    public LoginHandler(){};

    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        LoginService service = new LoginService();
        UserResult result = service.login(loginRequest);
        if (result.getMessage() == null) {
            response.status(200);
        }
        else if (result.getMessage().contains("Error")) {
            response.status(401);
        }
        return gson.toJson(result);
    }
}
