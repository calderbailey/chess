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
            default -> throw new DataAccessException("ERROR: team color is invalid", 500);
        };

        if (!(currentPlayer == null || currentPlayer.equals(username))) {
            String errorString = "ERROR: " + currentPlayer + " is already playing as " + color;
            throw new DataAccessException(errorString, 500);
        }

        if (currentOpponent != null && currentOpponent.equals(username)){
            String errorString = "ERROR: you are already playing as " + oppositeColor;
            throw new DataAccessException(errorString, 500);
        }
    }
}
