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
//        joinGameRequest.setGameID();
        joinGameRequest.setAuthToken(request.headers("Authorization"));
        JoinGameService service = new JoinGameService();
        MessageOnlyResult messageOnlyResult =  service.joinGame(joinGameRequest);
        if (messageOnlyResult.getMessage().isEmpty()){
            response.status(200);
            messageOnlyResult.setMessage(null);
        }
        else {response.status(400);}
//        return "{}";
        return gson.toJson(messageOnlyResult);
    }
}
