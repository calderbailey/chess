package dataaccess;

import chess.ChessGame;
import exceptionhandling.DataAccessException;
import handlers.CreateGameHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import requestresult.*;
import service.GameService;
import service.UserService;

import java.util.ArrayList;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests {
    private static final UserDAOInterface USER_DAO = new MemoryUserDAO();
    private static final AuthDAOInterface AUTH_DAO = new MemoryAuthDAO();
    private static final GameDAOInterface GAME_DAO;

    static {
        try {
            GAME_DAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
    }

//    @Test
//    @Order(1)
//    @DisplayName("Register: Success")
//    public void registerSuccess() throws DataAccessException{
//        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
//        new UserService().register(regReq);
//        UserData userData = USER_DAO.getUser("Username");
//        Assertions.assertEquals(new UserData("Username", "Password", "EMAIL@GMAIL.COM"), userData,
//                "Registration was not correctly added to the database");
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("Register: User Taken")
//    public void registerUserTaken() {
//        USER_DAO.createUser(new UserData("Username", "Password", "EMAIL@GMAIL.COM"));
//        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
//        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
//            new UserService().register(regReq);
//        });
//        Assertions.assertEquals("Error: already taken", exception.getDefaultMessage());
//        Assertions.assertEquals(403, exception.getStatusCode());
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("Login: Success")
//    public void loginSuccess() throws Exception{
//        USER_DAO.createUser(new UserData("Username", "Password", "email"));
//        LoginRequest logReq = new LoginRequest("Username", "Password");
//        LoginResult logRes = new UserService().login(logReq);
//        String authToken = logRes.authToken();
//        AuthData authData = AUTH_DAO.getAuth(authToken);
//        Assertions.assertEquals("Username", authData.username());
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("Login: Unauthorized")
//    public void loginUnauthorized() {
//        LoginRequest logReq = new LoginRequest("Username", "Password");
//        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
//            new UserService().login(logReq);
//        });
//        Assertions.assertEquals("Error: unauthorized", exception.getDefaultMessage());
//        Assertions.assertEquals(401, exception.getStatusCode());
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("Logout: Success")
//    public void logoutSuccess() throws Exception{
//        USER_DAO.createUser(new UserData("Username", "Password", "email"));
//        LoginRequest logReq = new LoginRequest("Username", "Password");
//        LoginResult logRes = new UserService().login(logReq);
//        LogoutResult logoutRes = new UserService().logout(new LogoutRequest(logRes.authToken()));
//        Assertions.assertEquals(new LogoutResult(), logoutRes);
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("Logout: Unauthorized")
//    public void logoutUnauthorized() {
//        LogoutRequest logReq = new LogoutRequest("wrong");
//        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
//            new UserService().logout(logReq);
//        });
//        Assertions.assertEquals("Error: unauthorized", exception.getDefaultMessage());
//        Assertions.assertEquals(401, exception.getStatusCode());
//    }

    @Test
    @Order(7)
    @DisplayName("createGame: Success")
    public void createGameSuccess() throws Exception{
        Integer createResult = GAME_DAO.createGame("Game 1");
        Assertions.assertNotNull(createResult);
    }

    @Test
    @Order(8)
    @DisplayName("createGame: Game Name Taken")
    public void createGameNameTaken() throws DataAccessException{
        GAME_DAO.createGame("Game 1");
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            GAME_DAO.createGame("Game 1");
        });
        Assertions.assertEquals("Error: game name taken", exception.getDefaultMessage());
        Assertions.assertEquals(500, exception.getStatusCode());
    }

    @Test
    @Order(9)
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

    @Test
    @Order(10)
    @DisplayName("getGameList: Unauthorized")
    public void getGameListUnauthorized() throws DataAccessException{
        //Authorization Check Occurs at the Handler Level.
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            new CreateGameHandler().checkAuth("wrong");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getDefaultMessage());
        Assertions.assertEquals(401, exception.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("getGame: Success")
    public void getGameSuccess() throws Exception{
        GameData testGame = new GameData(1, null, null, "Game 1", new ChessGame());
        GAME_DAO.createGame("Game 1");
        GameData game = GAME_DAO.getGame(1);
        Assertions.assertEquals(testGame, game);
    }

    @Test
    @Order(12)
    @DisplayName("getGame: GameID Doesn't Exist")
    public void getGameBadID() throws Exception{
        GameData game = GAME_DAO.getGame(1);
        Assertions.assertNull(game);
    }

    @Test
    @Order(13)
    @DisplayName("Clear: Success")
    public void clearSuccess() throws Exception{
        GAME_DAO.createGame("Game 1");
        GAME_DAO.createGame("Game 2");
        GAME_DAO.createGame("Game 3");
        GAME_DAO.clear();
        Assertions.assertEquals(0, GAME_DAO.getGameList().size());
    }
}

