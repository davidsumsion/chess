package handlers;

import com.google.gson.Gson;
import requests.JoinGameRequest;
import results.MessageOnlyResult;
import services.Exceptions.BadRequestException;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;
import spark.Request;
import spark.Response;
import services.JoinGameService;

public class JoinGameHandler {

    public JoinGameHandler(){};

    public Object handle(Request request, Response response){
        Gson gson = new Gson();
        JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        joinGameRequest.setAuthToken(request.headers("Authorization"));
        JoinGameService service = new JoinGameService();
        MessageOnlyResult messageOnlyResult = null;
        try {
            messageOnlyResult =  service.joinGame(joinGameRequest);
            response.status(200);
            messageOnlyResult.setMessage(null);
        } catch(UnauthorizedException e){
            response.status(401);
            messageOnlyResult = new MessageOnlyResult();
            messageOnlyResult.setMessage(e.getMessage());
        } catch (ForbiddenException e) {
            response.status(403);
            messageOnlyResult = new MessageOnlyResult();
            messageOnlyResult.setMessage(e.getMessage());
        } catch (BadRequestException e) {
            response.status(400);
            messageOnlyResult = new MessageOnlyResult();
            messageOnlyResult.setMessage(e.getMessage());
        }
        return gson.toJson(messageOnlyResult);
    }
}
