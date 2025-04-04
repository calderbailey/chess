package dataaccess;

import chess.ChessGame;
import exceptionhandling.DataAccessException;
import handlers.CreateGameHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import test.Test;
import java.util.ArrayList;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests extends Test {

    @org.junit.jupiter.api.Test
    @Order(1)
    @DisplayName("createAuth: Success")
    public void createAuthSuccess() throws DataAccessException{
        Assertions.assertNotNull(AUTH_DAO.createAuth("USER"));
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    @DisplayName("getAuth: Success")
    public void getAuthSuccess() throws DataAccessException{
        AuthData originalAuthData = AUTH_DAO.createAuth("USER");
        AuthData newAuthData = AUTH_DAO.getAuth(originalAuthData.authToken());
        Assertions.assertEquals(originalAuthData, newAuthData);
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    @DisplayName("getAuth: Authorization Doesn't Exist")
    public void getAuthBadRequest() throws DataAccessException{
        Assertions.assertNull(AUTH_DAO.getAuth("WRONG TOKEN"));
    }

    @org.junit.jupiter.api.Test
    @Order(4)
    @DisplayName("authDAOClear: Success")
    public void authDAOClearSuccess() throws DataAccessException{
        AuthData authData = AUTH_DAO.createAuth("USER");
        AUTH_DAO.clear();
        Assertions.assertNull(AUTH_DAO.getAuth(authData.authToken()));
    }

    @org.junit.jupiter.api.Test
    @Order(5)
    @DisplayName("delAuth: Success")
    public void delAuthSuccess() throws DataAccessException{
        AuthData authData = AUTH_DAO.createAuth("USER");
        AUTH_DAO.delAuth(authData.authToken());
        Assertions.assertNull(AUTH_DAO.getAuth(authData.authToken()));
    }

    @org.junit.jupiter.api.Test
    @Order(6)
    @DisplayName("getUser: Success")
    public void getUserSuccess() throws DataAccessException{
        UserData testUserData = new UserData("User", "Password", "email");
        USER_DAO.createUser(testUserData);
        UserData userData = USER_DAO.getUser(testUserData.username());
        Assertions.assertEquals(testUserData.username(), userData.username());
        Assertions.assertEquals(testUserData.email(), userData.email());
    }

    @org.junit.jupiter.api.Test
    @Order(7)
    @DisplayName("getUser: Does Not Exist")
    public void getUserBadRequest() throws DataAccessException{
        Assertions.assertNull(USER_DAO.getUser("WRONG USERNAME"));
    }

    @org.junit.jupiter.api.Test
    @Order(8)
    @DisplayName("createUser: Success")
    public void createUserSuccess() throws DataAccessException{
        getUserSuccess();
    }

    @org.junit.jupiter.api.Test
    @Order(9)
    @DisplayName("userDAOClear: Success")
    public void userDAOClearSuccess() throws Exception{
        USER_DAO.createUser(new UserData("User 1", "Password", "Email"));
        Assertions.assertNotNull(USER_DAO.getUser("User 1"));
        USER_DAO.clear();
        Assertions.assertNull(USER_DAO.getUser("User 1"));
    }

    @org.junit.jupiter.api.Test
    @Order(10)
    @DisplayName("createGame: Success")
    public void createGameSuccess() throws Exception{
        Integer createResult = GAME_DAO.createGame("Game 1");
        Assertions.assertNotNull(createResult);
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    @DisplayName("createGame: Game Name Taken")
    public void createGameNameTaken() throws DataAccessException{
        GAME_DAO.createGame("Game 1");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            GAME_DAO.createGame("Game 1");
        });
        Assertions.assertEquals("Error: game name taken", exception.getMessage());
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    @DisplayName("getGameList: Success")
    public void getGameListSuccess() throws Exception{

        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(new GameData(1, null, null, "Game 1", new ChessGame()));
        testList.add(new GameData(2, null, null, "Game 2", new ChessGame()));
        testList.add(new GameData(3, null, null, "Game 3", new ChessGame()));

        GAME_DAO.createGame("Game 1");
        GAME_DAO.createGame("Game 2");
        GAME_DAO.createGame("Game 3");
        ArrayList<GameData> allGames = GAME_DAO.getGameList();

        Assertions.assertEquals(testList, allGames);
    }

    @org.junit.jupiter.api.Test
    @Order(13)
    @DisplayName("getGameList: Unauthorized")
    public void getGameListUnauthorized() throws DataAccessException{
        //Authorization Check Occurs at the Handler Level.
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new CreateGameHandler().checkAuth("wrong");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(14)
    @DisplayName("getGame: Success")
    public void getGameSuccess() throws Exception{
        GameData testGame = new GameData(1, null, null, "Game 1", new ChessGame());
        GAME_DAO.createGame("Game 1");
        GameData game = GAME_DAO.getGame(1);
        Assertions.assertEquals(testGame, game);
    }

    @org.junit.jupiter.api.Test
    @Order(15)
    @DisplayName("getGame: GameID Doesn't Exist")
    public void getGameBadID() throws Exception{
        GameData game = GAME_DAO.getGame(1);
        Assertions.assertNull(game);
    }

    @org.junit.jupiter.api.Test
    @Order(16)
    @DisplayName("gameDAOClear: Success")
    public void gameDAOClearSuccess() throws Exception{
        GAME_DAO.createGame("Game 1");
        GAME_DAO.createGame("Game 2");
        GAME_DAO.createGame("Game 3");
        GAME_DAO.clear();
    }

    @org.junit.jupiter.api.Test
    @Order(17)
    @DisplayName("createGameID: Success")
    public void createGameIDSuccess() throws Exception{
        Integer gameID1 = GAME_DAO.createGameID();
        Integer gameID2 = GAME_DAO.createGameID();
        Integer gameID3 = GAME_DAO.createGameID();
        Assertions.assertEquals(1, gameID1);
        Assertions.assertEquals(2, gameID2);
        Assertions.assertEquals(3, gameID3);
    }

    @org.junit.jupiter.api.Test
    @Order(18)
    @DisplayName("updateGame: Success")
    public void updateGameSuccess() throws Exception{
        GameData testGame = new GameData(1, null, "USER", "Game 1", new ChessGame());
        GAME_DAO.createGame("Game 1");
        GAME_DAO.updateGame("USER", "BLACK", 1);
        GameData game = GAME_DAO.getGame(1);
        Assertions.assertEquals(testGame, game);
    }

    @org.junit.jupiter.api.Test
    @Order(19)
    @DisplayName("updateGame: Bad Request")
    public void updateGameBadRequest() throws Exception{
        GameData testGame = new GameData(1, null, "USER", "Game 1", new ChessGame());
        GAME_DAO.createGame("Game 1");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            GAME_DAO.updateGame("USER", "black", 1);        });
        Assertions.assertEquals("Error: bad request", exception.getMessage());
        Assertions.assertEquals(400, exception.getStatusCode());
    }

    @org.junit.jupiter.api.Test
    @Order(20)
    @DisplayName("colorAvailable: Success")
    public void colorAvailableSuccess() throws Exception{
        GAME_DAO.createGame("Game 1");
        GAME_DAO.updateGame("USER", "BLACK", 1);
        Assertions.assertDoesNotThrow(() -> GAME_DAO.isColorAvailable("USER", "BLACK", GAME_DAO.getGame(1)));
    }

    @org.junit.jupiter.api.Test
    @Order(21)
    @DisplayName("colorAvailable: Unavailable")
    public void colorAvailableUnavailable() throws Exception{
        GAME_DAO.createGame("Game 1");
        GAME_DAO.updateGame("USER", "BLACK", 1);
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            GAME_DAO.isColorAvailable("USERNAME", "BLACK", GAME_DAO.getGame(1));
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
        Assertions.assertEquals(403, exception.getStatusCode());
    }
}

