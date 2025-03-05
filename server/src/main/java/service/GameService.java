package service;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import requestresult.*;

public class GameService {
    private final GameDAOInterface gameDAO = new MemoryGameDAO();
    private final AuthDAOInterface authDAO = new MemoryAuthDAO();

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException {
        String newGameName = createRequest.gameName();
        Integer newGameID = gameDAO.createGame(newGameName);
        CreateResult createResult = new CreateResult(newGameID);
        return createResult;
    }

    public ListGamesResult listGames(ListGamesRequest listRequest) {
        ListGamesResult listRes = new ListGamesResult(gameDAO.getGameList());
        return listRes;
    }

    public JoinGameResult joinGame(JoinGameRequest joinRequest) throws DataAccessException {
        String username = joinRequest.username();
        String playerColor = joinRequest.playerColor();
        Integer gameID = joinRequest.gameID();
        gameDAO.updateGame(username, playerColor, gameID);
        JoinGameResult joinRes = new JoinGameResult();
        return joinRes;
    }
}
