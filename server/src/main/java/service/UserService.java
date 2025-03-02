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

    public RegisterResult register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        userDAO.createUser(new UserData(username, password, email));
        RegisterResult regRes = new RegisterResult(username, "null");
        return regRes;
    }

    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData usernameResult = userDAO.getUser(username);
        return new LoginResult(usernameResult.username(), "null");
    }
//    public void logout(LogoutRequest logoutRequest) {}
}