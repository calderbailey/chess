package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.UUID;

public class MySqlAuthDAO extends MySqlDAO implements AuthDAOInterface {

    public MySqlAuthDAO() throws DataAccessException {
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String updateStatement = "INSERT INTO auth (username, jsonAuthData) VALUES (?, ?)";
        AuthData authData = new AuthData(generateToken(), username);
        String json = new Gson().toJson(authData);
        executeUpdate(updateStatement, username, json);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void delAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        clearHelper("auth");
    }

    @Override
    protected String[] getCreateStatements() {
        return new String[]{
                """
            CREATE TABLE IF NOT EXISTS auth (
              `id` INT NOT NULL AUTO_INCREMENT,
              `username` VARCHAR(255) NOT NULL,
              `jsonAuthData` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

}
