package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {
    //NOTE RUN THE SERVER BEFORE!!
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() {
        // register a new user
        ServerFacade serverFacade = new ServerFacade();
        String result =  serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        Assertions.assertEquals("USERNAME", result);
    }

    @Test
    public void registerNegative() {
        //try to register an existing User
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        String result = serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        Assertions.assertEquals("User Already Exists", result);
    }

    @Test void loginPositive() {
        // create a user and then log in using the username
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        String result = serverFacade.login("USERNAME", "PASSWORD");
        Assertions.assertEquals("USERNAME", result);
    }

    @ Test
    public void loginNegative() {
        // log a user in that isn't registered
        ServerFacade serverFacade = new ServerFacade();
        String result = serverFacade.login("username", "password");
        Assertions.assertEquals("Username or Password Incorrect", result);
    }

    @Test
    public void logoutPositive() {
        // log out a logged in user
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        String result = serverFacade.logout();
        Assertions.assertEquals("", result);
    }

    @Test
    public void logoutNegative(){
        ServerFacade serverFacade = new ServerFacade();
        String result = serverFacade.logout();
        Assertions.assertEquals("Unauthorized: AuthToken not in Database", result);
    }

    @Test
    public void createGamePositive() {
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("user","pass", "email");
        String result = serverFacade.createGame("GAME");
        Assertions.assertEquals("1", result);
    }

    @Test
    public void createGameNegative() {
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME");
        String result = serverFacade.createGame("GAME");
        Assertions.assertEquals("Game Already Exists or Unauthorized", result);
    }

    @Test
    public void listGamesPositive() {
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        serverFacade.createGame("GAME B");
        serverFacade.createGame("GAME C");
        String result = serverFacade.listGames();
        Assertions.assertEquals("ID\t\tGame Name\t\t\tWhite Username\t\tBlack Username\n" +
                "1\t\t\tGAME A\t\tnull\t\t\tnull\n" +
                "2\t\t\tGAME B\t\tnull\t\t\tnull\n" +
                "3\t\t\tGAME C\t\tnull\t\t\tnull\n", result);
    }

    @Test
    public void listGamesNegativee() {
        ServerFacade serverFacade = new ServerFacade();
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        serverFacade.createGame("GAME B");
        serverFacade.createGame("GAME D");
        String result = serverFacade.listGames();
        Assertions.assertNotEquals("ID\t\tGame Name\t\t\tWhite Username\t\tBlack Username\n" +
                "1\t\t\tGAME A\t\tnull\t\t\tnull\n" +
                "2\t\t\tGAME B\t\tnull\t\t\tnull\n" +
                "3\t\t\tGAME C\t\tnull\t\t\tnull\n", result);
    }


}
