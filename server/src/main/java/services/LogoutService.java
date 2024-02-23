package services;

import dataAccess.MemoryAuthTokenDA;
import requests.AuthTokenRequest;
import results.UserResult;
import services.Exceptions.BadRequestException;

public class LogoutService {
    public LogoutService() {}
    public UserResult logout(AuthTokenRequest request) throws BadRequestException {
        MemoryAuthTokenDA dao = new MemoryAuthTokenDA();
        boolean result = dao.deleteSession(request.getAuthToken());
        if (!result){ throw new BadRequestException("Error: unable to delete"); }
        else { return new UserResult(null, null); }
    }
}
