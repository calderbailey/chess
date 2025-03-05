package service;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.UserData;
import requestresult.*;

import javax.xml.crypto.Data;

public class UserService {
    private final UserDAOInterface USER_DAO = new MemoryUserDAO();
    private final AuthDAOInterface AUTH_DAO = new MemoryAuthDAO();
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (username == null | password == null | email == null) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (USER_DAO.getUser(username) != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        USER_DAO.createUser(new UserData(username, password, email));
        AuthData authData = AUTH_DAO.createAuth(username);
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
        UserData usernameResult = USER_DAO.getUser(username);
        if (usernameResult == null || !usernameResult.password().equals(password)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        AuthData authData = AUTH_DAO.createAuth(username);
        String authToken = authData.authToken();
        return new LoginResult(usernameResult.username(), authToken);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        String authToken = logoutRequest.authToken();
        AUTH_DAO.delAuth(authToken);
        return new LogoutResult();
    }

    public ClearResult clear(ClearRequest clearRequest) {
        new MemoryAuthDAO().clear();
        new MemoryUserDAO().clear();
        new MemoryGameDAO().clear();
        return new ClearResult();
    }


}