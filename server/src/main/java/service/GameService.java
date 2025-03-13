package service;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import requestresult.*;

public class GameService<gameDAO> extends Service{

    private static final GameDAOInterface GAME_DAO;

    static {
        if ("MySql".equals(DATABASE_METHOD)) {
            try {
                GAME_DAO = new MySqlGameDAO();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            GAME_DAO = new MemoryGameDAO();
            throw new RuntimeException();

        }
    }

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException {
        String newGameName = createRequest.gameName();
        Integer newGameID = GAME_DAO.createGame(newGameName);
        CreateResult createResult = new CreateResult(newGameID);
        return createResult;
    }

    public ListGamesResult listGames(ListGamesRequest listRequest) throws DataAccessException {
        ListGamesResult listRes = new ListGamesResult(GAME_DAO.getGameList());
        return listRes;
    }

    public JoinGameResult joinGame(JoinGameRequest joinRequest) throws DataAccessException {
        String username = joinRequest.username();
        String playerColor = joinRequest.playerColor();
        Integer gameID = joinRequest.gameID();
        GAME_DAO.updateGame(username, playerColor, gameID);
        JoinGameResult joinRes = new JoinGameResult();
        return joinRes;
    }
}
