package services;

import dataAccess.MemoryAuthTokenDA;
import requests.AuthTokenRequest;
import results.UserResult;

public class LogoutService {
    public LogoutService() {
    }

    public UserResult logout(AuthTokenRequest request) {
        MemoryAuthTokenDA dao = new MemoryAuthTokenDA();
        boolean result = dao.deleteSession(request.getAuthToken());
        if (!result){
            UserResult user =  new UserResult(null, null);
            user.setMessage("Error: unable to delete");
            return user;
        }
        else {
            return new UserResult(null, null);
        }
    }
}
