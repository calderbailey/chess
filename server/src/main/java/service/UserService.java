package service;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.*;

public class UserService extends Service{

    private final static UserDAOInterface USER_DAO;
    private final static AuthDAOInterface AUTH_DAO;
    private final static GameDAOInterface GAME_DAO;

    static {
        if ("MySql".equals(DATABASE_METHOD)) {
            try {
                USER_DAO = new MySqlUserDAO();
                AUTH_DAO = new MySqlAuthDAO();
                GAME_DAO = new MySqlGameDAO();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            USER_DAO = new MemoryUserDAO();
            AUTH_DAO = new MemoryAuthDAO();
            GAME_DAO = new MemoryGameDAO();
        }
    }

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
        if (usernameResult == null || !verifyPassword(usernameResult, password)) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        AuthData authData = AUTH_DAO.createAuth(username);
        String authToken = authData.authToken();
        return new LoginResult(usernameResult.username(), authToken);
    }

    private boolean verifyPassword(UserData userData, String password) {
        var hashedPassword = userData.password();
        return BCrypt.checkpw(password, hashedPassword);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        String authToken = logoutRequest.authToken();
        AUTH_DAO.delAuth(authToken);
        return new LogoutResult();
    }

    public ClearResult clear(ClearRequest clearRequest) throws DataAccessException{
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
        return new ClearResult();
    }
}