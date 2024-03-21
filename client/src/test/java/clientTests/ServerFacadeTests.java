package clientTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class ServerFacadeTests {
    private static Server server;
    private static String port;

    private static final String databaseName;
//    private static final String user;
//    private static final String password;
//    private static final String connectionUrl;

    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
//                user = props.getProperty("db.user");
//                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
//                port = Integer.parseInt(props.getProperty("db.port"));
//                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }
    @AfterEach
    public void cleanUp() {
        try (Connection conn = DatabaseManager.getConnection()){
            String sql = "DROP DATABASE IF EXISTS " + databaseName + ";";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            DatabaseManager.createDatabase();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @BeforeAll
    public static void init() {
        server = new Server();
//        String[] args = new String[]{"nothing"};
//        server.main(args);
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ServerFacadeTests.port = String.valueOf(port);
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
        ServerFacade serverFacade = new ServerFacade(port);

        String result =  serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        Assertions.assertEquals("USERNAME", result);
    }

    @Test
    public void registerNegative() {
        //try to register an existing User
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        String result = serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        Assertions.assertEquals("User Already Exists", result);
    }

    @Test void loginPositive() {
        // create a user and then log in using the username
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        String result = serverFacade.login("USERNAME", "PASSWORD");
        Assertions.assertEquals("USERNAME", result);
    }

    @ Test
    public void loginNegative() {
        // log a user in that isn't registered
        ServerFacade serverFacade = new ServerFacade(port);
        String result = serverFacade.login("username", "password");
        Assertions.assertEquals("Username or Password Incorrect", result);
    }

    @Test
    public void logoutPositive() {
        // log out a logged in user
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("USERNAME", "PASSWORD", "EMAIL@GMAIL.COM");
        String result = serverFacade.logout();
        Assertions.assertEquals("", result);
    }

    @Test
    public void logoutNegative(){
        ServerFacade serverFacade = new ServerFacade(port);
        String result = serverFacade.logout();
        Assertions.assertEquals("Unauthorized: AuthToken not in Database", result);
    }

    @Test
    public void createGamePositive() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        String result = serverFacade.createGame("GAME");
        Assertions.assertEquals("1", result);
    }

    @Test
    public void createGameNegative() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME");
        String result = serverFacade.createGame("GAME");
        Assertions.assertEquals("Game Already Exists or Unauthorized", result);
    }

    @Test
    public void listGamesPositive() {
        ServerFacade serverFacade = new ServerFacade(port);
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
        ServerFacade serverFacade = new ServerFacade(port);
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

    @Test void joinGamePositive() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        String result = serverFacade.joinGamePlayer("1", "BLACK");
        Assertions.assertEquals("", result);
    }

    @Test
    public void  joinGameNegative() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        String result = serverFacade.joinGamePlayer("2", "BLACK");
        Assertions.assertEquals("Error Game", result);
    }

    @Test
    public void  joinGameNegative2() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        serverFacade.joinGamePlayer("1", "BLACK");
        String result = serverFacade.joinGamePlayer("1", "BLACK");
        Assertions.assertEquals("Error Color", result);
    }

    @Test
    public void  joinGameNegative3() {
        ServerFacade serverFacade = new ServerFacade(port);
        String result = serverFacade.joinGamePlayer("1", "BLACK");
        Assertions.assertEquals("Unauthorized", result);
    }

    @Test
    public void  joinGameObserverPositive() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        String result = serverFacade.joinGamePlayer("1", null);
        Assertions.assertEquals("", result);
    }

    @Test
    public void joinGameObserverNegative() {
        ServerFacade serverFacade = new ServerFacade(port);
        serverFacade.register("user","pass", "email");
        serverFacade.createGame("GAME A");
        String result = serverFacade.joinGamePlayer("2", "BLACK");
        Assertions.assertEquals("Error Game", result);
    }

    @Test
    public void joinGameObserverNegative2() {
        ServerFacade serverFacade = new ServerFacade(port);
        String result = serverFacade.joinGamePlayer("1", "BLACK");
        Assertions.assertEquals("Unauthorized", result);
    }
}
