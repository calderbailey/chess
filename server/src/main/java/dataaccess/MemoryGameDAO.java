package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAOInterface{
    private static final Map<Integer, GameData> gameMap = new HashMap<>();
    private static Integer nextGameID = 1;

    @Override
    public Integer createGame(String gameName) {
        GameData newGame = new GameData(createGameID(),null, null, gameName, new ChessGame());
        gameMap.put(newGame.gameID(), newGame);
        return newGame.gameID();
    }

    @Override
    public GameData getGame(Integer gameID) {
        return null;
    }

    @Override
    public void delGame(Integer gameID) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Integer createGameID(){
        nextGameID ++;
        return nextGameID - 1;
    }
}
