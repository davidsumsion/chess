package services;
import requests.RegisterRequest;
import spark.Response;
import spark.Request;
import models.UserDAOModel;
import dataAccess.*;

public class RegisterService implements UserService {
    public RegisterService(){}

    public Response register(RegisterRequest request, Response response){
        UserDAOModel user = new UserDAOModel("myUsername", "myPassword", "myEmail");
        MemoryUserDao dao = new MemoryUserDao(user);
        String nullUser = dao.getUser();
        if (nullUser == null){
            dao.createUser();
            dao.createAuthToken();
            Response rr = new Response();
//            return new ArrayList(user.getUsername(), user.getAuthToken());
        } else {
            return null;
        }



        return null;
    }

}
