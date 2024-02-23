package myUnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import requests.*;
import results.CreateGameResult;
import results.MessageOnlyResult;
import results.UserResult;
import services.*;

public class myServiceTests {


    /////////////////////
    ////Register Tests///
    /////////////////////
    @Test
    @DisplayName("Register New User")
    public void registerNewUser() {
        RegisterRequest registerRequestA = new RegisterRequest("testUsername3", "testPassword3", "testEmail");
        RegisterService registerServiceA = new RegisterService();
        UserResult userResultA =  registerServiceA.register(registerRequestA);
        Assertions.assertEquals("testUsername3", userResultA.getUsername(), "Username was not testUsername");
//        Assertions.assertNotNull(UserResult.getAuthToken(), "authToken is NULL");
        Assertions.assertNull(userResultA.getMessage(), "A message was set");
    }

    @Test
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
    @DisplayName("Login Existing User")
    public void LoginExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest("testLoginUsername3", "testLoginPassword3", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("testLoginUsername3", "testLoginPassword3");
        loginRequest.setAuthToken(userResult.getAuthToken());

        LoginService loginService = new LoginService();
        UserResult userResult1 = loginService.login(loginRequest);

        Assertions.assertEquals("testLoginUsername3", userResult1.getUsername(), "Incorrect Username");
        Assertions.assertNotEquals(userResult.getAuthToken(), userResult1.getAuthToken(), "Incorrect AuthToken");
    }

    @Test
    @DisplayName("Login Nonexisting User")
    public void LoginNonExistingUser() {
        LoginRequest loginRequest = new LoginRequest("testLoginUsername1", "testLoginPassword1");

        LoginService loginService = new LoginService();
        UserResult userResult1 = loginService.login(loginRequest);

        Assertions.assertEquals(null, userResult1.getUsername(), "Incorrect Username");
    }

    @Test
    @DisplayName("Logout User")
    public void LogoutExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameLogout1", "testPasswordLogout1", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        LogoutService logoutService = new LogoutService();
        UserResult userResultLogout = logoutService.logout(new AuthTokenRequest(userResult.getAuthToken()));

        Assertions.assertNull(userResultLogout.getUsername(), "Incorrect Username");
    }

    ///////////////////////
    ////CREATE GAMES///////
    ///////////////////////

    @Test
    @DisplayName("Create Game")
    public void CreateGame() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame3", "testPasswordCreateGame3", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        Assertions.assertNull(createGameResult.getMessage(), "Incorrect Username");
    }

    @Test
    @DisplayName("Create Game Same name")
    public void CreateGameSameName() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame2", "testPasswordCreateGame2", "testEmail");
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
    @DisplayName("Create Game Not Authorized")
    public void CreateGameNotAuthorized() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame1", "testPasswordCreateGame1", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken("1234");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        Assertions.assertEquals("Error: Not Authorized", createGameResult.getMessage(), "Incorrect Username");
    }

    @Test
    @DisplayName("Join Game")
    public void JoinGame() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame3", "testPasswordCreateGame3", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest(createGameResult.getGameID(),"WHITE");
        joinGameRequest.setAuthToken(userResult.getAuthToken());
        JoinGameService joinGameService = new JoinGameService();
        MessageOnlyResult messageOnlyResult = joinGameService.joinGame(joinGameRequest);

        Assertions.assertEquals("", messageOnlyResult.getMessage(), "Error when expected none");
    }





}
