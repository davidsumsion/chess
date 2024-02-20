package handlers;

import com.google.gson.Gson;
import requests.JoinGameRequest;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    public JoinGameHandler(){};

    public Object handle(Request request, Response response){
        Gson gson = new Gson();
        JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        JoinGameService service = new JoinGameService();
        service.joinGame(joinGameRequest);

    }
}
