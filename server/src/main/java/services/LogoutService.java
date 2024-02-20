package services;

import dataAccess.MemorySessionDA;
import requests.AuthTokenRequest;
import results.UserResult;

public class LogoutService {
    public LogoutService(){};

    public UserResult logout(AuthTokenRequest request){
        MemorySessionDA dao = new MemorySessionDA(null);
        String res = dao.deleteSesh(request.getAuthToken());
        if (res.equals(null)){
            UserResult user =  new UserResult(null, null);
            user.setMessage("unable to delete");
            return user;
        }
        else {
            return new UserResult(null, null);
        }
    }

}
