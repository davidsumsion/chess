package handlers;

import com.google.gson.Gson;
import requests.JoinGameRequest;
import results.MessageOnlyResult;
import spark.Request;
import spark.Response;
import services.JoinGameService;

public class JoinGameHandler {

    public JoinGameHandler(){};

    public Object handle(Request request, Response response){
        Gson gson = new Gson();
        JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        JoinGameService service = new JoinGameService();
        MessageOnlyResult mess =  service.joinGame(joinGameRequest);
        return mess;
    }
}
