package ui;

import chess.ChessGame;
import exceptionhandling.DataAccessException;
import model.GameData;
import requestresult.*;
import chess.ChessGame.TeamColor;
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
        updateGameList();
        if (gameList.keySet().isEmpty() || gameList.keySet() == null) {
            return "No active games\n";
        } else {
            return mapToString(gameList).toString();
        }
    }

    private void updateGameList() throws DataAccessException {
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
    }

    private StringBuilder mapToString(HashMap<Integer, GameData> gameList) {
        StringBuilder fullString = new StringBuilder();
        for (Integer key : gameList.keySet()) {
            fullString.append(gameToString(key));
        }
        return fullString;
    }

    private String gameToString(Integer key) {
        String gameName = gameList.get(key).gameName();
        String whiteUser = gameList.get(key).whiteUsername();
        String blackUser = gameList.get(key).blackUsername();
        return key + ". " + gameName + "\n" +
                "   White: " + whiteUser + "\n" +
                "   Black: " + blackUser + "\n";
    }

    public String join(String[] userInput) throws DataAccessException {
        Integer gameID;
        String gameName;
        Integer gameNum;
        try {
            gameNum = Integer.parseInt(userInput[1]);
            gameID = gameList.get(gameNum).gameID();
            gameName = gameList.get(gameNum).gameName();
        } catch (Exception e) {
            throw new DataAccessException("Invalid game ID", 500);
        }
        String teamColor = userInput[2].toUpperCase();
        if (!(teamColor.equals("BLACK") | teamColor.equals("WHITE"))) {
            throw new DataAccessException("Invalid player color", 500);
        }
        JoinGameRequest joinRequest = new JoinGameRequest(teamColor, gameID);
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        JoinGameResult joinResult = serverFacade.joinGame(joinRequest, authToken);
        updateGameList();
        return gameToString(gameNum);
    }
}
