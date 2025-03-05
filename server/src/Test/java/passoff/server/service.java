package passoff.server;

import chess.ChessGame;
import dataaccess.AuthDAOInterface;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAOInterface;
import exceptionhandling.DataAccessException;
import handlers.Handler;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.RegisterRequest;
import service.UserService;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class service {
    private static final UserDAOInterface userDAO = new MemoryUserDAO();
    private static final AuthDAOInterface authDAO = new MemoryAuthDAO();

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
}

