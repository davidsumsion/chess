package handlers;

import requests.RegisterRequest;
import com.google.gson.Gson;
import results.UserResult;
import services.ForbiddenException;
import services.RegisterService;
import services.UnauthorizedException;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        RegisterService service = new RegisterService();
        UserResult result = null;
        try{
            result = service.register(registerRequest);
            response.status(200);
        } catch (UnauthorizedException e) {
            result = new UserResult(null,null);
            result.setMessage(e.getMessage());
            response.status(400);
        } catch (ForbiddenException e) {
            result = new UserResult(null,null);
            result.setMessage(e.getMessage());
            response.status(403);
        }
        return gson.toJson(result);
    }
}
