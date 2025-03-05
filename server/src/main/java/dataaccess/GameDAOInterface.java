package dataaccess;

import exceptionhandling.DataAccessException;
import model.GameData;
import java.util.ArrayList;

public interface GameDAOInterface {
    Integer createGame(String gameName) throws DataAccessException;
    GameData getGame(Integer gameID);
    void delGame(Integer gameID);
    void clear();
    Integer createGameID();
    ArrayList<GameData> getGameList();
    void colorAvailable(String color, Integer gameID) throws DataAccessException;
    void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException;
}
