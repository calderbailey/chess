package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAOInterface;
import exceptionhandling.DataAccessException;
import model.UserData;
import requestresult.*;

public class UserService {
    private static final UserDAOInterface userDAO = new MemoryUserDAO();

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