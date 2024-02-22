package myTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.UserResult;
import services.RegisterService;

public class myServiceTests {

    @Test
    @Order(1)
    @DisplayName("Register New User")
    public void registerNewUser() {
        RegisterRequest registerRequestA = new RegisterRequest("testUsername", "testPassword", "testEmail");
        RegisterService registerServiceA = new RegisterService();
        UserResult userResultA =  registerServiceA.register(registerRequestA);
        Assertions.assertEquals("testUsername", userResultA.getUsername(), "Username was not testUsername");
//        Assertions.assertNotNull(UserResult.getAuthToken(), "authToken is NULL");
        Assertions.assertNull(userResultA.getMessage(), "A message was set");
    }

    @Test
    @Order(2)
    @DisplayName("Register Existing User")
    public void registerExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest("testUsername", "testPassword", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        //same Username
        RegisterRequest registerRequestB = new RegisterRequest("testUsername", "testPasswordB", "testEmail");
        RegisterService registerServiceB = new RegisterService();
        UserResult userResultB =  registerServiceB.register(registerRequestB);

        Assertions.assertNotNull(userResultB.getMessage());
    }






}
