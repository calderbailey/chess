package ui;

import exceptionhandling.DataAccessException;
import model.GameData;
import requestresult.*;

import java.util.HashMap;

public class ChessClient {
    private static String authToken;
    private static HashMap<Integer, GameData> gameList;
    private final String serverUrl;
    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        gameList = new HashMap<>();
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

    public String List() throws DataAccessException {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        ListGamesResult listResult = serverFacade.listGames(new ListGamesRequest(), authToken);
        if (gameList != null) {
            gameList.clear();
        }
        Integer keyNum = 1;
        for (GameData game : listResult.games()) {
            gameList.put(keyNum, game);
            keyNum ++;
        }
        if (gameList.keySet().isEmpty() || gameList.keySet() == null) {
            return "No active games\n";
        } else {
            return mapToString(gameList).toString();
        }
    }

    private StringBuilder mapToString(HashMap<Integer, GameData> gameList) {
        StringBuilder fullString = new StringBuilder();
        for (Object key : gameList.keySet()) {
            String gameName = gameList.get(key).gameName();
            String whiteUser = gameList.get(key).whiteUsername();
            String blackUser = gameList.get(key).blackUsername();
            String gameString = key + ". " + gameName + "\n" +
                                "   White: " + whiteUser + "\n" +
                                "   Black: " + blackUser + "\n";
            fullString.append(gameString);
        }
        return fullString;
    }
}
