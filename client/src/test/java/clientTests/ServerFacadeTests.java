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

}
