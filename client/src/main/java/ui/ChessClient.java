package ui;

import exceptionhandling.DataAccessException;
import requestresult.*;

public class ChessClient {
    private static String authToken;
    private final String serverUrl;
    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String Register(String[] userInput) throws DataAccessException {
        String username = userInput[1];
        String password = userInput[2];
        String email = userInput[3];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult regResult = serverFacade.register(registerRequest);
        authToken = regResult.authToken();
        return "Logged in as " + username;
    }

    public String Login(String[] userInput) throws DataAccessException {
        String username = userInput[1];
        String password = userInput[2];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverFacade.login(loginRequest);
        authToken = loginResult.authToken();
        return "Logged in as " + username;
    }

    public String Create(String[] userInput) throws DataAccessException {
        String gameName = userInput[1];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        CreateRequest createRequest = new CreateRequest(gameName);
        serverFacade.createGame(createRequest, authToken);
        return gameName + " has been created";
    }
}
