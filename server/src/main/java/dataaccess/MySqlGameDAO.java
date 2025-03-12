package dataaccess;

import exceptionhandling.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public class MySqlGameDAO implements GameDAOInterface{
    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(Integer gameID) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public Integer createGameID() {
        return 0;
    }

    @Override
    public ArrayList<GameData> getGameList() {
        return null;
    }

    @Override
    public void colorAvailable(String color, Integer gameID) throws DataAccessException {

    }

    @Override
    public void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException {

    }
}
