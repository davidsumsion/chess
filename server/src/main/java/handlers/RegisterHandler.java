package handlers;

import requests.RegisterRequest;
import com.google.gson.Gson;
import results.UserResult;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    //read data in, call object encoder/decoder
    //make a result object and return it
    //make a request object and return it

    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        RegisterService service = new RegisterService();
        UserResult result = service.register(registerRequest);
        response.status(200);
        return gson.toJson(result);
    }
}
