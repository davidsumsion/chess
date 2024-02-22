package myTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import requests.AuthTokenRequest;
import requests.CreateGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.CreateGameResult;
import results.UserResult;
import services.CreateGameService;
import services.LoginService;
import services.LogoutService;
import services.RegisterService;

public class myServiceTests {


    /////////////////////
    ////Register Tests///
    /////////////////////
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


    ///////////////////
    // LOGIN TESTS ////
    ///////////////////
    @Test
    @Order(3)
    @DisplayName("Login Existing User")
    public void LoginExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest("testLoginUsername", "testLoginPassword", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("testLoginUsername", "testLoginPassword");
        loginRequest.setAuthToken(userResult.getAuthToken());

        LoginService loginService = new LoginService();
        UserResult userResult1 = loginService.login(loginRequest);

        Assertions.assertEquals("testLoginUsername", userResult1.getUsername(), "Incorrect Username");
        Assertions.assertNotEquals(userResult.getAuthToken(), userResult1.getAuthToken(), "Incorrect AuthToken");
    }

    @Test
    @Order(4)
    @DisplayName("Login Nonexisting User")
    public void LoginNonExistingUser() {
        LoginRequest loginRequest = new LoginRequest("testLoginUsername", "testLoginPassword");

        LoginService loginService = new LoginService();
        UserResult userResult1 = loginService.login(loginRequest);

        Assertions.assertEquals(null, userResult1.getUsername(), "Incorrect Username");
    }

    @Test
    @Order(5)
    @DisplayName("Logout User")
    public void LogoutExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameLogout", "testPasswordLogout", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        LogoutService logoutService = new LogoutService();
        UserResult userResultLogout = logoutService.logout(new AuthTokenRequest(userResult.getAuthToken()));

        Assertions.assertEquals(null, userResultLogout.getUsername(), "Incorrect Username");
    }

    @Test
    @Order(6)
    @DisplayName("Create Game")
    public void CreateGame() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame", "testPasswordCreateGame", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);


        Assertions.assertEquals(null, createGameResult.getMessage(), "Incorrect Username");
    }

    @Test
    @Order(7)
    @DisplayName("Create Game Same name")
    public void CreateGameSameName() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame", "testPasswordCreateGame", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        CreateGameRequest createGameRequestSameName = new CreateGameRequest("MyNewGame");
        createGameRequestSameName.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameServiceSameName = new CreateGameService();
        CreateGameResult createGameResultSameName = createGameServiceSameName.createGame(createGameRequestSameName);
        Assertions.assertEquals("Error: Game Name Already in use", createGameResultSameName.getMessage(), "Incorrect Username");
    }

    @Test
    @Order(7)
    @DisplayName("Create Game Not Authorized")
    public void CreateGameNotAuthorized() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame", "testPasswordCreateGame", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken("1234");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        Assertions.assertEquals("Error: Not Authorized", createGameResult.getMessage(), "Incorrect Username");
    }





}
