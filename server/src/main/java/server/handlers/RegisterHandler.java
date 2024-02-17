package server.handlers;

import server.requests.RegisterRequest;
import com.google.gson.Gson;


public class RegisterHandler {
    //read data in, call object encoder/decoder
    //make a result object and return it
    //make a request object and return it
    Gson gson = new Gson();
    String reqData = args[0];
    RegisterRequest request = (RegisterRequest)gson.fromJson(reqData, RegisterRequest.class);

//    RegisterService service = new RegisterService();
//    RegisterResult result = service.register(reqeust);
//    return gson.toJson(result);
}
