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

    default void isAlreadyPlaying(String username, Integer gameID) throws DataAccessException{
        String blackUser = getGame(gameID).blackUsername();
        String whiteUser = getGame(gameID).whiteUsername();
        if (username.equals(blackUser) | username.equals(whiteUser)) {
            String errorString = "ERROR: " + username + " is already a player in that game";
            throw new DataAccessException(errorString, 500);
        }
    }
}
