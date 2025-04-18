package service;
import exceptionhandling.DataAccessException;
import handlers.CreateGameHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import requestresult.*;
import test.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests extends Test {

    @org.junit.jupiter.api.Test
    @Order(1)
    @DisplayName("Register: Success")
    public void registerSuccess() throws DataAccessException{
        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
        new UserService().register(regReq);
        UserData userData = USER_DAO.getUser("Username");
        Assertions.assertEquals("Username", userData.username(),
                "Registration was not correctly added to the database");
        Assertions.assertEquals("EMAIL@GMAIL.COM", userData.email(),
                "Registration was not correctly added to the database");
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    @DisplayName("Register: User Taken")
    public void registerUserTaken() throws DataAccessException {
        USER_DAO.createUser(new UserData("Username", "Password", "EMAIL@GMAIL.COM"));
        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new UserService().register(regReq);
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    @DisplayName("Login: Success")
    public void loginSuccess() throws Exception{
        USER_DAO.createUser(new UserData("Username", "Password", "email"));
        LoginRequest logReq = new LoginRequest("Username", "Password");
        LoginResult logRes = new UserService().login(logReq);
        String authToken = logRes.authToken();
        AuthData authData = AUTH_DAO.getAuth(authToken);
        Assertions.assertEquals("Username", authData.username());
    }

    @org.junit.jupiter.api.Test
    @Order(4)
    @DisplayName("Login: Unauthorized")
    public void loginUnauthorized() {
        LoginRequest logReq = new LoginRequest("Username", "Password");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new UserService().login(logReq);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(5)
    @DisplayName("Logout: Success")
    public void logoutSuccess() throws Exception{
        USER_DAO.createUser(new UserData("Username", "Password", "email"));
        LoginRequest logReq = new LoginRequest("Username", "Password");
        LoginResult logRes = new UserService().login(logReq);
        LogoutResult logoutRes = new UserService().logout(new LogoutRequest(logRes.authToken()));
        Assertions.assertEquals(new LogoutResult(), logoutRes);
    }

    @org.junit.jupiter.api.Test
    @Order(6)
    @DisplayName("Logout: Unauthorized")
    public void logoutUnauthorized() {
        LogoutRequest logReq = new LogoutRequest("wrong");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new UserService().logout(logReq);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(7)
    @DisplayName("CreateGame: Success")
    public void createGameSuccess() throws Exception{
        Integer createResult = GAME_DAO.createGame("Game 1");
        Assertions.assertNotNull(createResult);
    }

    @org.junit.jupiter.api.Test
    @Order(8)
    @DisplayName("CreateGame: Game Name Taken")
    public void createGameNameTaken() throws DataAccessException{
        GAME_DAO.createGame("Game 1");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new GameService().createGame(new CreateRequest("Game 1"));
        });
        Assertions.assertEquals("Error: game name taken", exception.getMessage());
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(9)
    @DisplayName("ListGames: Success")
    public void listGamesSuccess() throws Exception{
        GAME_DAO.createGame("Game 1");
        GAME_DAO.createGame("Game 2");
        GAME_DAO.createGame("Game 3");
        ListGamesResult listRes = new GameService().listGames(new ListGamesRequest());
        Assertions.assertEquals(3, listRes.games().size());
    }

    @org.junit.jupiter.api.Test
    @Order(10)
    @DisplayName("ListGames: Unauthorized")
    public void listGamesUnauthorized() throws DataAccessException{
        //Authorization Check Occurs at the Handler Level.
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new CreateGameHandler().checkAuth("wrong");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    @DisplayName("JoinGame: Success")
    public void joinGameSuccess() throws Exception{
        GAME_DAO.createGame("Game 1");
        JoinGameRequest joinReq = new JoinGameRequest("WHITE", 1, "USER");
        new GameService().joinGame(joinReq);
        GameData gameData = GAME_DAO.getGame(1);
        Assertions.assertEquals("USER", gameData.whiteUsername());
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    @DisplayName("JoinGame: Already Taken")
    public void joinGameAlreadyTaken() throws Exception{
        GAME_DAO.createGame("Game 1");
        GAME_DAO.updateGame("USER1", "WHITE", 1);
        JoinGameRequest joinReq = new JoinGameRequest("WHITE", 1, "USER2");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new GameService().joinGame(joinReq);
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(13)
    @DisplayName("Clear: Success")
    public void clearSuccess() throws Exception{
        GAME_DAO.createGame("Game 1");
        USER_DAO.createUser(new UserData("Username", "Password", "Email"));
       AuthData authData = AUTH_DAO.createAuth("Username");
       String authToken = authData.authToken();
       new UserService().clear(new ClearRequest());
       Assertions.assertNull(GAME_DAO.getGame(1));
       Assertions.assertNull(USER_DAO.getUser("Username"));
       Assertions.assertNull(AUTH_DAO.getAuth(authToken));
    }
}

