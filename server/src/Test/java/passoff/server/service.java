package passoff.server;

import chess.ChessGame;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAOInterface;
import exceptionhandling.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;
import requestresult.RegisterRequest;
import service.UserService;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class service {
    private static final UserDAOInterface userDAO = new MemoryUserDAO();
    @Test
    @Order(1)
    @DisplayName("Register: Success")
    public void registerSuccess() {
        RegisterRequest regReq = new RegisterRequest("Username", "Password", "EMAIL@GMAIL.COM");
        try {
            new UserService().register(regReq);
        } catch (Exception e) {
            System.out.printf(e.toString());
        }
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
}

