package dataaccess;

import chess.ChessGame;
import exceptionhandling.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAOInterface{
    private static final Map<Integer, GameData> GAME_MAP = new HashMap<>();
    private static Integer nextGameID = 1;

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        GameData newGame = new GameData(createGameID(),null, null, gameName, new ChessGame());
        for (Integer gameID : GAME_MAP.keySet()) {
            if (GAME_MAP.get(gameID).gameName() == gameName) {
                throw new DataAccessException("Error: game name taken", 500);
            }
        }
        GAME_MAP.put(newGame.gameID(), newGame);
        return newGame.gameID();
    }

    @Override
    public GameData getGame(Integer gameID) {
        return GAME_MAP.get(gameID);
    }

    @Override
    public void setGame(Integer gameID, GameData game) {
        GAME_MAP.replace(gameID, game);
    }

    @Override
    public void clear() {
        GAME_MAP.clear();
        nextGameID = 1;
    }

    @Override
    public Integer createGameID(){
        nextGameID ++;
        return nextGameID - 1;
    }

    @Override
    public ArrayList<GameData> getGameList() {
        ArrayList<GameData> gameList = new ArrayList<>();
        for (Integer gameID : GAME_MAP.keySet()) {
            GameData game = GAME_MAP.get(gameID);
            gameList.add(game);
        }
        return gameList;
    }

    @Override
    public void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException{
        GameData game = GAME_MAP.get(gameID);
        isColorAvailable(username, playerColor, game);
        GameData updatedGame;
        if (playerColor.equals("WHITE")) {
            updatedGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
        } else {
            updatedGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
        }
        GAME_MAP.remove(gameID);
        GAME_MAP.put(gameID, updatedGame);
    }
}
