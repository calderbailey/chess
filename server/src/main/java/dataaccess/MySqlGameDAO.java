package dataaccess;

import exceptionhandling.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public class MySqlGameDAO extends MySqlDAO implements GameDAOInterface {
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

    @Override
    protected final String[] getCreateStatements() {
        return new String[]{
                """
            CREATE TABLE IF NOT EXISTS  Games (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL,
              `jsonChessGame` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
    }
}
