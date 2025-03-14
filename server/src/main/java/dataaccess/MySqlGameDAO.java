package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptionhandling.*;
import model.GameData;
import java.util.ArrayList;

public class MySqlGameDAO extends MySqlDAO implements GameDAOInterface {

    public MySqlGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        if (isNameUnique(gameName)) {
            String updateStatement = "INSERT INTO games (gameID, jsonGameData) VALUES (?, ?)";
            Integer gameID = createGameID();
            GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
            String json = new Gson().toJson(newGame);
            executeUpdate(updateStatement, gameID, json);
            return gameID;
        } else {
            throw new DataAccessException("Error: game name taken", 500);            }
    }

    private boolean isNameUnique(String gameName) throws DataAccessException {
        ArrayList<GameData> allGames = getAllGames();
        for (GameData game : allGames) {
            if (gameName.equals(game.gameName())) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<GameData> getAllGames() throws DataAccessException{
        ArrayList<GameData> allGames = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT jsonGameData FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("jsonGameData");
                        GameData gameData = new Gson().fromJson(json, GameData.class);
                        allGames.add(gameData);
                    }
                    return allGames;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT jsonGameData FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("jsonGameData");
                        return new Gson().fromJson(json, GameData.class);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException{
        clearHelper("games", "nextGameID");
    }

    @Override
    public Integer createGameID() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID FROM nextGameID WHERE id = 1";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Integer gameID = rs.getInt("gameID");
                        var updateStatement = "UPDATE nextGameID SET gameID = ? WHERE id = 1";
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
    public ArrayList<GameData> getGameList() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT jsonGameData FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    ArrayList<GameData> gameList = new ArrayList<>();
                    while (rs.next()) {
                        String gameDataJson = rs.getString(1);
                        GameData gameData = new Gson().fromJson(gameDataJson, GameData.class);
                        gameList.add(gameData);
                    }
                    return gameList;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
    }

    @Override
    public void colorAvailable(String color, Integer gameID) throws DataAccessException {
        if (color == null | gameID == null) {
            throw new DataAccessException("Error: bad request", 400);
        } else if (!(color.equals("WHITE") | color.equals("BLACK"))) {
            throw new DataAccessException("Error: bad request", 400);
        } else {
            GameData game = getGame(gameID);
            if (!((color.equals("WHITE") & game.whiteUsername() == null) |
                (color.equals("BLACK") & game.blackUsername() == null))) {
                throw new DataAccessException("Error: already taken", 403);
            }
        }
    }

    @Override
    public void updateGame(String username, String playerColor, Integer gameID) throws DataAccessException {
        colorAvailable(playerColor, gameID);
        GameData game = getGame(gameID);
        GameData updatedGame;
        if (playerColor.equals("WHITE")) {
            updatedGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
        } else if (playerColor.equals("BLACK")) {
            updatedGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
        } else {
            throw new DataAccessException("Error: bad request", 400);
        }
        String gameJson = new Gson().toJson(updatedGame, GameData.class);
        String statement = "UPDATE games SET jsonGameData = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, gameJson);
            ps.setInt(2, gameID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
    }

    @Override
    protected final String[] getCreateStatements() {
        return new String[]{
                """
            CREATE TABLE IF NOT EXISTS  games (
              `id` INT NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL,
              `jsonGameData` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
                """
            CREATE TABLE IF NOT EXISTS  nextGameID (
              `id` INT NOT NULL AUTO_INCREMENT,
              `gameID` INT NOT NULL DEFAULT 1,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
                """
            INSERT INTO nextGameID (gameID) VALUES (DEFAULT)
            """
        };
    }
}
