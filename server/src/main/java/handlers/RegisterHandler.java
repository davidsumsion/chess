package handlers;

import requests.RegisterRequest;
import com.google.gson.Gson;
import services.RegisterService;
import spark.Request;
import spark.Response;
//import spark.Route;



public class RegisterHandler {
    //read data in, call object encoder/decoder
    //make a result object and return it
    //make a request object and return it

    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(registerRequest, response);
//        response.status(200);
//        return gson.toJson(result);
        return null;
    }


}
