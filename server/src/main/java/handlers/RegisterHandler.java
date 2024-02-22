package handlers;

import requests.RegisterRequest;
import com.google.gson.Gson;
import results.UserResult;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handle(Request request, Response response) throws Exception {
        //decode
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        //create service and call register
        RegisterService service = new RegisterService();
        UserResult result = service.register(registerRequest);
        //set proper status code
        if (!result.getUsername().isEmpty()){
            response.status(200);
        }
        else { response.status(400); }

        return gson.toJson(result);
    }
}
