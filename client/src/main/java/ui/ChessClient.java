package ui;

import exceptionhandling.DataAccessException;
import model.GameData;
import requestresult.*;
import ui.websocket.WebSocketFacade;
import java.util.HashMap;

public class ChessClient{
    private static String authToken;
    private static String username;
    private static HashMap<Integer, GameData> gameList;
    private final String serverUrl;
    private WebSocketFacade ws;
    private GameData gameData;
    private GamePlayRepl gamePlayRepl;
    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        gameList = new HashMap<>();
    }
    public ChessClient(String serverUrl, GamePlayRepl gamePlayRepl) {
        this.serverUrl = serverUrl;
        this.gamePlayRepl = gamePlayRepl;
        gameList = new HashMap<>();
    }

    public String register(String[] userInput) throws DataAccessException {
        username = userInput[1];
        String password = userInput[2];
        String email = userInput[3];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult regResult = serverFacade.register(registerRequest);
        authToken = regResult.authToken();
        return "Logged in as " + username;
    }

    public String login(String[] userInput) throws DataAccessException {
        username = userInput[1];
        String password = userInput[2];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverFacade.login(loginRequest);
        authToken = loginResult.authToken();
        return "Logged in as " + username;
    }

    public String create(String[] userInput) throws DataAccessException {
        String gameName = userInput[1];
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        CreateRequest createRequest = new CreateRequest(gameName);
        serverFacade.createGame(createRequest, authToken);
        return gameName + " has been created";
    }

    public String list() throws DataAccessException {
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

    public void join(String[] userInput) throws DataAccessException {
        updateGameList();
        Integer gameID;
        Integer gameNum;
        try {
            gameNum = Integer.parseInt(userInput[1]);
            gameID = gameList.get(gameNum).gameID();
        } catch (Exception e) {
            throw new DataAccessException("ERROR: invalid game ID", 500);
        }
        String teamColor = userInput[2].toUpperCase();
        if (!(teamColor.equals("BLACK") | teamColor.equals("WHITE"))) {
            throw new DataAccessException("ERROR: invalid player color", 500);
        }
        JoinGameRequest joinRequest = new JoinGameRequest(teamColor, gameID);
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        serverFacade.joinGame(joinRequest, authToken);
        updateGameList();
        ws = new WebSocketFacade(serverUrl, gamePlayRepl, teamColor);
        gamePlayRepl.setWS(ws);
        ws.connect(authToken, gameID);
    }

    public void observe(String[] userInput) throws DataAccessException {
        updateGameList();
        Integer gameID;
        Integer gameNum;
        try {
            gameNum = Integer.parseInt(userInput[1]);
            gameID = gameList.get(gameNum).gameID();
        } catch (Exception e) {
            throw new DataAccessException("ERROR: invalid game ID", 500);
        }
        String teamColor = null;
        ws = new WebSocketFacade(serverUrl, gamePlayRepl, teamColor);
        gamePlayRepl.setWS(ws);
        ws.connect(authToken, gameID);
    }

    public String getUsername(){
        return username;
    }
}
