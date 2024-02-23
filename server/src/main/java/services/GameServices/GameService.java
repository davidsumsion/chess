package services.GameServices;

import dataAccess.MemoryAuthTokenDA;
import services.Exceptions.UnauthorizedException;

public class GameService {
    public GameService(){};
    public String findUsername(String authToken){
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        return memoryAuthTokenDA.getUser(authToken);
    }
    public void verifyAuthToken(String authToken) throws UnauthorizedException{
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        boolean exists = memoryAuthTokenDA.verifyAuthToken(authToken);
        if (!exists){ throw new UnauthorizedException("Error: Not Authorized"); }
    }
}
