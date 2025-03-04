package service;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import requestresult.*;

public class GameService {
    private final GameDAOInterface gameDAO = new MemoryGameDAO();

    public CreateResult createGame(CreateRequest createRequest) {
        String newGameName = createRequest.gameName();
        Integer newGameID = gameDAO.createGame(newGameName);
        CreateResult createResult = new CreateResult(newGameID);
        return createResult;
    }
//
//    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
//        String username = loginRequest.username();
//        String password = loginRequest.password();
//        if (username == null | password == null) {
//            throw new DataAccessException("Error: bad request", 400);
//        }
//        UserData usernameResult = userDAO.getUser(username);
//        if (usernameResult == null || !usernameResult.password().equals(password)) {
//            throw new DataAccessException("Error: unauthorized", 401);
//        }
//        AuthData authData = authDAO.createAuth(username);
//        String authToken = authData.authToken();
//        return new LoginResult(usernameResult.username(), authToken);
//    }
//    public LogoutResult logout(LogoutRequest logoutRequest) {
//        String authToken = logoutRequest.authToken();
//        authDAO.delAuth(authToken);
//        return new LogoutResult();
//    }
}
