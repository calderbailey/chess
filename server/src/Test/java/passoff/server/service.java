package passoff.server;

import chess.ChessGame;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAOInterface;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import requestresult.RegisterRequest;
import service.UserService;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class service {
    private static final UserDAOInterface userDAO = new MemoryUserDAO();
    @Test
    @Order(1)
    @DisplayName("Register Positive")
    public void registerPositive() {
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
}

