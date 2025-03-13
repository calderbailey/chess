package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptionhandling.*;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlGameDAO extends MySqlDAO implements GameDAOInterface {

    public MySqlGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, jsonChessGame) VALUES (?, ?)";
        Integer gameID = createGameID();
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        String json = new Gson().toJson(newGame);
        executeUpdate(statement, gameID, json);
        return gameID;
    }

    @Override
    public GameData getGame(Integer gameID) {
        return null;
    }

    @Override
    public void clear() {
    }

    @Override
    public Integer createGameID() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID FROM nextGameID";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Integer gameID = rs.getInt("gameID");
                        var updateStatement = "UPDATE nextGameID SET gameID = ?";
                        var updatedGameID = gameID + 1;
                        try (var updatedPS = conn.prepareStatement(updateStatement)) {
                            updatedPS.setInt(1, updatedGameID);
                            updatedPS.executeUpdate();
                        }
                        return gameID;
                    } else {
                        throw new DataAccessException("No gameID found in nextGameID table.", 500);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
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
            CREATE TABLE IF NOT EXISTS  games (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL DEFAULT 0,
              `jsonChessGame` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
                """
            CREATE TABLE IF NOT EXISTS  nextGameID (
              `gameID` int NOT NULL DEFAULT 1
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
                """
            INSERT INTO nextGameID (gameID) VALUES (DEFAULT)
            """

        };
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()), 500);
        }
    }
}
