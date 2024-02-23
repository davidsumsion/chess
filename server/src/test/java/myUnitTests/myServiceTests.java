package myUnitTests;
import models.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.MessageOnlyResult;
import results.UserResult;
import services.*;
import services.Exceptions.BadRequestException;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;
import services.GameServices.CreateGameService;
import services.GameServices.JoinGameService;
import services.GameServices.ListGamesService;
import services.UserServices.LoginService;
import services.LogoutService;
import services.UserServices.RegisterService;


public class myServiceTests {

    @AfterEach
    public void cleanUp() {
        DeleteAllService deleteAllService = new DeleteAllService();
        deleteAllService.deleteAll();
        GameData gameData = new GameData();
        gameData.resetCounter();
    }

    /////////////////////
    ////Register Tests///
    /////////////////////
    @Test
    @DisplayName("Register New User")
    public void registerNewUser() {
        RegisterRequest registerRequestA = new RegisterRequest("testUsername3", "testPassword3", "testEmail");
        RegisterService registerServiceA = new RegisterService();
        UserResult userResultA = null;
        try {
            userResultA = registerServiceA.register(registerRequestA);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (ForbiddenException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("testUsername3", userResultA.getUsername(), "Username was not testUsername");
//        Assertions.assertNotNull(UserResult.getAuthToken(), "authToken is NULL");
        Assertions.assertNull(userResultA.getMessage(), "A message was set");
    }

    @Test
    @DisplayName("Register Existing User")
    public void registerExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest("testUsername", "testPassword", "testEmail");
        RegisterService registerService = new RegisterService();
        try {
            UserResult userResult =  registerService.register(registerRequest);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (ForbiddenException e) {
            throw new RuntimeException(e);
        }

        //same Username
        RegisterRequest registerRequestB = new RegisterRequest("testUsername", "testPasswordB", "testEmail");
        RegisterService registerServiceB = new RegisterService();
        UserResult userResultB = null;
        try {
            userResultB = registerServiceB.register(registerRequestB);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (ForbiddenException e) {
            userResultB = new UserResult(null, null);
            userResultB.setMessage(e.getMessage());
        }
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
        UserResult userResult = null;
        try {
            userResult = registerService.register(registerRequest);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (ForbiddenException e) {
            throw new RuntimeException(e);
        }

        LoginRequest loginRequest = new LoginRequest("testLoginUsername3", "testLoginPassword3");
        loginRequest.setAuthToken(userResult.getAuthToken());

        LoginService loginService = new LoginService();
        UserResult userResult1 = null;
        try {
            userResult1 = loginService.login(loginRequest);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("testLoginUsername3", userResult1.getUsername(), "Incorrect Username");
        Assertions.assertNotEquals(userResult.getAuthToken(), userResult1.getAuthToken(), "Incorrect AuthToken");
    }

    @Test
    @DisplayName("Login Nonexisting User")
    public void LoginNonExistingUser() {
        LoginRequest loginRequest = new LoginRequest("testLoginUsername1", "testLoginPassword1");

        LoginService loginService = new LoginService();
        UserResult userResult1 = null;
        try {
            userResult1 = loginService.login(loginRequest);
        } catch (UnauthorizedException e) {
            userResult1 = new UserResult(null, null);
            userResult1.setMessage(e.getMessage());
        }

        Assertions.assertEquals(null, userResult1.getUsername(), "Incorrect Username");
    }

    @Test
    @DisplayName("Logout User")
    public void LogoutExistingUser() throws ForbiddenException, UnauthorizedException, BadRequestException {
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
    public void CreateGame() throws ForbiddenException, UnauthorizedException {
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
    public void CreateGameSameName() throws UnauthorizedException, ForbiddenException {
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
        CreateGameResult createGameResultSameName = null;
        try {
            createGameResultSameName = createGameServiceSameName.createGame(createGameRequestSameName);
        } catch (UnauthorizedException e) {
            createGameResultSameName = new CreateGameResult(e.getMessage());
        }
        Assertions.assertEquals("Error: Game Name Already in use", createGameResultSameName.getMessage(), "Incorrect Username");
    }

    @Test
    @DisplayName("Create Game Not Authorized")
    public void CreateGameNotAuthorized() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame1", "testPasswordCreateGame1", "testEmail");
        RegisterService registerService = new RegisterService();
        try {
            UserResult userResult =  registerService.register(registerRequest);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (ForbiddenException e) {
            throw new RuntimeException(e);
        }

        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken("1234");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = null;
        try {
            createGameResult = createGameService.createGame(createGameRequest);
        } catch (UnauthorizedException e) {
            createGameResult = new CreateGameResult(e.getMessage());
        }

        Assertions.assertEquals("Error: Not Authorized", createGameResult.getMessage(), "Incorrect Username");
    }

    ///////////////////////
    ///////Join game///////
    ///////////////////////
    @Test
    @DisplayName("Join Game")
    public void JoinGame() throws ForbiddenException, UnauthorizedException, BadRequestException {
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

    /////////////////////////
    ///////Delete All///////
    /////////////////////////
    @Test
    @DisplayName("Delete All")
    public void DeleteAll() throws ForbiddenException, UnauthorizedException {
        //new user and auth
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame3", "testPasswordCreateGame3", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        //new game
        CreateGameRequest createGameRequest = new CreateGameRequest("MyNewGame");
        createGameRequest.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        DeleteAllService deleteAllService = new DeleteAllService();
        deleteAllService.deleteAll();

        //Test User Memory
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginService loginService = new LoginService();
        UserResult userResult1 = null;
        try {
            userResult1 = loginService.login(loginRequest);
        } catch (UnauthorizedException e) {
            userResult1 = new UserResult(null, null);
            userResult1.setMessage(e.getMessage());
        }
        Assertions.assertEquals("Error: User not found in database. Register before logging in", userResult1.getMessage(), "Error occured");

        //Test Game Memory
        JoinGameRequest joinGameRequest = new JoinGameRequest(createGameResult.getGameID(), "WHITE");
        JoinGameService joinGameService = new JoinGameService();
        MessageOnlyResult messageOnlyResult = null;
        try {
            messageOnlyResult = joinGameService.joinGame(joinGameRequest);
        } catch (UnauthorizedException e) {
            messageOnlyResult = new MessageOnlyResult();
            messageOnlyResult.setMessage(e.getMessage());
        } catch (ForbiddenException e) {
            messageOnlyResult = new MessageOnlyResult();
            messageOnlyResult.setMessage(e.getMessage());
        } catch (BadRequestException e) {
            messageOnlyResult = new MessageOnlyResult();
            messageOnlyResult.setMessage(e.getMessage());
        }
        Assertions.assertNotNull(messageOnlyResult.getMessage(), "No error when there should have been");

        //Test AuthToken Memory
        AuthTokenRequest authTokenRequest = new AuthTokenRequest(userResult.getAuthToken());
        ListGamesService listGamesService = new ListGamesService();
        ListGamesResult listGamesResult = null;
        try {
            listGamesResult = listGamesService.listGames(authTokenRequest);
        } catch (UnauthorizedException e) {
            listGamesResult = new ListGamesResult(e.getMessage());
        }
        Assertions.assertEquals("Error: Not Authorized", listGamesResult.getMessage(), "Incorrect Message");

    }

    ///////////////////////
    ////LIST GAMES///////
    ///////////////////////
    @Test
    @DisplayName("List Games")
    public void ListGames() throws ForbiddenException, UnauthorizedException {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameCreateGame3", "testPasswordCreateGame3", "testEmail");
        RegisterService registerService = new RegisterService();
        UserResult userResult =  registerService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("JUST ");
        createGameRequest.setAuthToken(userResult.getAuthToken());
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest1 = new CreateGameRequest("NOT A ");
        createGameRequest1.setAuthToken(userResult.getAuthToken());
        CreateGameResult createGameResult1 = createGameService.createGame(createGameRequest1);

        CreateGameRequest createGameRequest3 = new CreateGameRequest("SUPER ONE");
        createGameRequest3.setAuthToken(userResult.getAuthToken());
        CreateGameResult createGameResult3 = createGameService.createGame(createGameRequest3);

        AuthTokenRequest authTokenRequest = new AuthTokenRequest(userResult.getAuthToken());
        ListGamesService listGamesService = new ListGamesService();
        ListGamesResult listGamesResult = listGamesService.listGames(authTokenRequest);


        Assertions.assertEquals(
                "[{gameID=1, gameName='JUST ', whiteUsername='null', blackUsername='null'}, {gameID=2, gameName='NOT A ', whiteUsername='null', blackUsername='null'}, {gameID=3, gameName='SUPER ONE', whiteUsername='null', blackUsername='null'}]"
                , listGamesResult.getGames().toString(),  "Didin't return games");
    }





}
