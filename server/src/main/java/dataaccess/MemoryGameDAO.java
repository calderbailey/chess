package dataaccess;

import chess.ChessGame;
import exceptionhandling.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAOInterface{
    private static final Map<Integer, GameData> gameMap = new HashMap<>();
    private static Integer nextGameID = 1;

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        GameData newGame = new GameData(createGameID(),null, null, gameName, new ChessGame());
        for (Integer gameID : gameMap.keySet()) {
            if (gameMap.get(gameID).gameName() == gameName) {
                throw new DataAccessException("Error: game name taken", 500);
            }
        }
        gameMap.put(newGame.gameID(), newGame);
        return newGame.gameID();
    }

    @Override
    public GameData getGame(Integer gameID) {
        return gameMap.get(gameID);
    }

    @Override
    public void delGame(Integer gameID) {
        gameMap.remove(gameID);
    }

    @Override
    public void clear() {
        gameMap.clear();
    }

    @Override
    public Integer createGameID(){
        nextGameID ++;
        return nextGameID - 1;
    }

    @Override
    public ArrayList<GameData> getGameList() {
        ArrayList<GameData> gameList = new ArrayList<>();
        for (Integer gameID : gameMap.keySet()) {
            GameData game = gameMap.get(gameID);
            gameList.add(game);
        }
        return gameList;
    }

    @Override
    public void colorAvailable(String color, Integer gameID) throws DataAccessException{
        GameData game = gameMap.get(gameID);
        if (color == null | gameID == null) {
            throw new DataAccessException("Error: bad request", 400);
        } else if (!(color.equals("WHITE") | color.equals("BLACK"))) {
            throw new DataAccessException("Error: bad request", 400);
        } else if (!((color.equals("WHITE") & game.whiteUsername() == null) |
                (color.equals("BLACK") & game.blackUsername() == null))) {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    @Override
    public void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException{
        colorAvailable(playerColor, gameID);
        GameData game = gameMap.get(gameID);
        GameData updatedGame;
        if (playerColor.equals("WHITE")) {
            updatedGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
        } else {
            updatedGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
        }
        gameMap.remove(gameID);
        gameMap.put(gameID, updatedGame);
    }
}
