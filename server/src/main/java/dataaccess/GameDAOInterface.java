package dataaccess;

import exceptionhandling.DataAccessException;
import model.GameData;
import java.util.ArrayList;

public interface GameDAOInterface {
    Integer createGame(String gameName) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    void clear() throws DataAccessException;
    Integer createGameID() throws DataAccessException;
    ArrayList<GameData> getGameList() throws DataAccessException;
    void colorAvailable(String color, Integer gameID) throws DataAccessException;
    void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException;
}
