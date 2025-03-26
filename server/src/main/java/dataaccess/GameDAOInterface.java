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
    void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException;

    default void isColorAvailable(String username, String color, GameData gameData) throws DataAccessException{
        String currentPlayer;
        String currentOpponent;
        String oppositeColor;
        if (color != null) {
            switch (color.toUpperCase()) {
                case "BLACK" -> {
                    currentPlayer = gameData.blackUsername();
                    currentOpponent = gameData.whiteUsername();
                    oppositeColor = "WHITE";
                }
                case "WHITE" -> {
                    currentPlayer = gameData.whiteUsername();
                    currentOpponent = gameData.blackUsername();
                    oppositeColor = "BLACK";
                }
                default -> throw new DataAccessException("ERROR: bad request", 400);
            }
        } else throw new DataAccessException("Error: bad request", 400);


        if (!(currentPlayer == null || currentPlayer.equals(username))) {
            String errorString = "Error: already taken";
            throw new DataAccessException(errorString, 403);
        }
    }
}
