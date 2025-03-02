package service;

import InMemoryDatabases.UserDatabase;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAOInterface;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import requestresult.*;
import dataaccess.UserDAOInterface.*;

public class UserService {
    private static final UserDAOInterface userDAO = new MemoryUserDAO();

    //    public RegisterResult register(RegisterRequest registerRequest) {}
    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        userDAO.createUser(new UserData(username, password, "null"));
        UserData usernameResult = userDAO.getUser(username);
        return new LoginResult(usernameResult.username(), "null");
    }
//    public void logout(LogoutRequest logoutRequest) {}
}