package service;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import model.AuthData;
import requestresult.*;

public class GameService {
    private final GameDAOInterface gameDAO = new MemoryGameDAO();
    private final AuthDAOInterface authDAO = new MemoryAuthDAO();

    public CreateResult createGame(CreateRequest createRequest) {
        String newGameName = createRequest.gameName();
        Integer newGameID = gameDAO.createGame(newGameName);
        CreateResult createResult = new CreateResult(newGameID);
        return createResult;
    }

    public ListGamesResult listGames(ListGamesRequest listRequest) {
        ListGamesResult listRes = new ListGamesResult(gameDAO.getGameList());
        return listRes;
    }
}
