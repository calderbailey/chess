package passoff.server;

import chess.ChessGame;
import dataaccess.*;
import exceptionhandling.DataAccessException;
import handlers.CreateGameHandler;
import handlers.Handler;
import handlers.LoginHandler;
import handlers.LogoutHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import requestresult.*;
import service.UserService;
import service.GameService;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class service {
    private static final UserDAOInterface userDAO = new MemoryUserDAO();
    private static final AuthDAOInterface authDAO = new MemoryAuthDAO();
    private static final GameDAOInterface gameDAO = new MemoryGameDAO();

    @Test
    @Order(1)
    @DisplayName("Register: Success")
    public void registerSuccess() throws DataAccessException{
        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
        new UserService().register(regReq);
        UserData userData = userDAO.getUser("Username");
        Assertions.assertEquals(new UserData("Username", "Password", "EMAIL@GMAIL.COM"), userData,
                "Registration was not correctly added to the database");
    }

    @Test
    @Order(2)
    @DisplayName("Register: User Taken")
    public void registerUserTaken() {
        userDAO.createUser(new UserData("Username", "Password", "EMAIL@GMAIL.COM"));
        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new UserService().register(regReq);
        });
        Assertions.assertEquals("Error: already taken", exception.getDefaultMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("Login: Success")
    public void loginSuccess() throws Exception{
        userDAO.createUser(new UserData("Username", "Password", "email"));
        LoginRequest logReq = new LoginRequest("Username", "Password");
        LoginResult logRes = new UserService().login(logReq);
        String authToken = logRes.authToken();
        AuthData authData = authDAO.getAuth(authToken);
        Assertions.assertEquals("Username", authData.username());
    }

    @Test
    @Order(4)
    @DisplayName("Login: Unauthorized")
    public void loginUnauthorized() {
        LoginRequest logReq = new LoginRequest("Username", "Password");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new UserService().login(logReq);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getDefaultMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Logout: Success")
    public void logoutSuccess() throws Exception{
        userDAO.createUser(new UserData("Username", "Password", "email"));
        LoginRequest logReq = new LoginRequest("Username", "Password");
        LoginResult logRes = new UserService().login(logReq);
        LogoutResult logoutRes = new UserService().logout(new LogoutRequest(logRes.authToken()));
        Assertions.assertEquals(new LogoutResult(), logoutRes);
    }

    @Test
    @Order(6)
    @DisplayName("Logout: Unauthorized")
    public void logoutUnauthorized() {
        LogoutRequest logReq = new LogoutRequest("wrong");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new UserService().logout(logReq);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getDefaultMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    @Order(7)
    @DisplayName("CreateGame: Success")
    public void createGameSuccess() throws Exception{
        Integer createResult = gameDAO.createGame("Game 1");
        Assertions.assertNotNull(createResult);
    }

    @Test
    @Order(8)
    @DisplayName("CreateGame: Game Name Taken")
    public void createGameNameTaken() throws DataAccessException{
        gameDAO.createGame("Game 1");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new GameService().createGame(new CreateRequest("Game 1"));
        });
        Assertions.assertEquals("Error: game name taken", exception.getDefaultMessage());
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("ListGames: Success")
    public void listGamesSuccess() throws Exception{
        gameDAO.createGame("Game1");
        gameDAO.createGame("Game 2");
        gameDAO.createGame("Game 3");
        ListGamesResult listRes = new GameService().listGames(new ListGamesRequest());
        Assertions.assertEquals(3, listRes.games().size());
    }

    @Test
    @Order(10)
    @DisplayName("ListGames: Unauthorized")
    public void listGamesUnauthorized() throws DataAccessException{
        //Authorization Check Occurs at the Handler Level.
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new CreateGameHandler().checkAuth("wrong");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getDefaultMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("JoinGame: Success")
    public void joinGameSuccess() throws Exception{
        gameDAO.createGame("Game 1");
        JoinGameRequest joinReq = new JoinGameRequest("WHITE", 1, "USER");
        new GameService().joinGame(joinReq);
        GameData gameData = gameDAO.getGame(1);
        Assertions.assertEquals("USER", gameData.whiteUsername());
    }

    @Test
    @Order(12)
    @DisplayName("JoinGame: Already Taken")
    public void joinGameAlreadyTaken() throws Exception{
        gameDAO.createGame("Game 1");
        gameDAO.updateGame("USER1", "WHITE", 1);
        JoinGameRequest joinReq = new JoinGameRequest("WHITE", 1, "USER2");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new GameService().joinGame(joinReq);
        });
        Assertions.assertEquals("Error: already taken", exception.getDefaultMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }
}

