package services.GameServices;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthTokenDA;
import dataAccess.MySqlAuthTokenDA;
import services.Exceptions.UnauthorizedException;

import java.sql.Connection;

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

    public void verifyAuthToken(Connection connection, String authToken) throws UnauthorizedException{
        try {
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
            boolean exists = mySqlAuthTokenDA.verifyAuthToken(connection, authToken);
            if (!exists) {
                throw new UnauthorizedException("Error: Not Authorized");
            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
