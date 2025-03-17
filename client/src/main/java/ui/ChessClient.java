package ui;

import exceptionhandling.DataAccessException;
import requestresult.LoginRequest;
import requestresult.RegisterRequest;

public class ChessClient {

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
        serverFacade.register(registerRequest);
        return "Logged in as " + username;
    }

    public String Login(String[] userInput) throws DataAccessException {
        String username = userInput[1];
        String password = userInput[2];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        LoginRequest loginRequest = new LoginRequest(username, password);
        serverFacade.login(loginRequest);
        return "Logged in as " + username;
    }
}
