package service;

import dataaccess.AuthDAOInterface;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAOInterface;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.UserData;
import requestresult.*;

import javax.xml.crypto.Data;

public class UserService {
    private final UserDAOInterface userDAO = new MemoryUserDAO();
    private final AuthDAOInterface authDAO = new MemoryAuthDAO();
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (username == null | password == null | email == null) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (userDAO.getUser(username) != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        userDAO.createUser(new UserData(username, password, email));
        AuthData authData = authDAO.createAuth(username);
        String authToken = authData.authToken();
        RegisterResult regRes = new RegisterResult(username, authToken);
        return regRes;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
        String username = loginRequest.username();
        String password = loginRequest.password();
        if (username == null | password == null) {
            throw new DataAccessException("Error: bad request", 400);
        }
        UserData usernameResult = userDAO.getUser(username);
        if (usernameResult == null || !usernameResult.password().equals(password)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        AuthData authData = authDAO.createAuth(username);
        String authToken = authData.authToken();
        return new LoginResult(usernameResult.username(), authToken);
    }
    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        String authToken = logoutRequest.authToken();
        authDAO.delAuth(authToken);
        return new LogoutResult();
    }

}