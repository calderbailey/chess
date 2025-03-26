package client;

import exceptionhandling.DataAccessException;
import org.junit.jupiter.api.*;
import requestresult.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String baseUrl = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + baseUrl);
        facade = new ServerFacade(baseUrl);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        facade.delete(new ClearRequest());
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void clearPositive() throws Exception {
        facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        facade.delete(new ClearRequest());
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerPositive() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerNegative() throws Exception {
        facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }

    @Test
    void loginPositive() throws Exception {
        facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        var authData = facade.login(new LoginRequest("USERNAME", "PASSWORD"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginNegative() throws Exception {
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            facade.login(new LoginRequest("USERNAME", "PASSWORD"));
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    void logoutPositive() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        LogoutResult logoutResult = facade.logout(new LogoutRequest(authData.authToken()), authData.authToken());
        assertEquals(new LogoutResult(), logoutResult);
    }

    @Test
    void logoutNegative() throws Exception {
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            facade.logout(new LogoutRequest("WRONG"), "WRONG");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    void createPositive() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        CreateRequest createRequest = new CreateRequest("GAME");
        CreateResult createResult = facade.createGame(createRequest, authData.authToken());
        Assertions.assertNotNull(createResult);
    }

    @Test
    void createNegative() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        CreateRequest createRequest = new CreateRequest("GAME");
        facade.createGame(createRequest, authData.authToken());
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            facade.createGame(createRequest, authData.authToken());
        });
        Assertions.assertEquals("Error: game name taken", exception.getMessage());
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    void listPositive() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        CreateRequest createRequest1 = new CreateRequest("GAME1");
        facade.createGame(createRequest1, authData.authToken());
        CreateRequest createRequest2 = new CreateRequest("GAME2");
        facade.createGame(createRequest2, authData.authToken());
        CreateRequest createRequest3 = new CreateRequest("GAME3");
        facade.createGame(createRequest3, authData.authToken());
        ListGamesResult listResult = facade.listGames(new ListGamesRequest(), authData.authToken());
        Assertions.assertEquals(3, listResult.games().size());
    }

    @Test
    void listNegative() throws Exception {
        CreateRequest createRequest1 = new CreateRequest("GAME1");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            facade.createGame(createRequest1, "WRONG");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    void joinPositive() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        CreateRequest createRequest1 = new CreateRequest("GAME1");
        CreateResult result = facade.createGame(createRequest1, authData.authToken());
        ListGamesResult gamesResult = facade.listGames(new ListGamesRequest(), authData.authToken());
        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", result.gameID());
        facade.joinGame(joinRequest, authData.authToken());
        ListGamesResult listResult = facade.listGames(new ListGamesRequest(), authData.authToken());
        Assertions.assertEquals(listResult.games().get(0).whiteUsername(), "USERNAME");
    }

    @Test
    void joinNegative() throws Exception {
        var authData = facade.register(new RegisterRequest("USERNAME", "PASSWORD", "EMAIL"));
        CreateRequest createRequest1 = new CreateRequest("GAME1");
        CreateResult result = facade.createGame(createRequest1, authData.authToken());
        ListGamesResult gamesResult = facade.listGames(new ListGamesRequest(), authData.authToken());
        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", result.gameID());
        facade.joinGame(joinRequest, authData.authToken());
        var authData1 = facade.register(new RegisterRequest("USERNAME1", "PASSWORD", "EMAIL"));
        JoinGameRequest joinRequest1 = new JoinGameRequest("WHITE", result.gameID());
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            facade.joinGame(joinRequest1, authData1.authToken());
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }
}

