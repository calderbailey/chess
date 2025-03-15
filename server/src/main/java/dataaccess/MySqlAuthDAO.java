package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.UUID;

public class MySqlAuthDAO extends MySqlDAO implements AuthDAOInterface {

    public MySqlAuthDAO() throws DataAccessException {
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        AuthData authData = new AuthData(generateToken(), username);
        String json = new Gson().toJson(authData);
        String updateStatement = "INSERT INTO auth (username, jsonAuthData) VALUES (?, ?)";
        executeUpdate(updateStatement, username, json);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        ArrayList<AuthData> allAuth = getAllAuth();
        for (AuthData authData : allAuth) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        return null;
    }

    private ArrayList<AuthData> getAllAuth() throws DataAccessException{
        ArrayList<AuthData> allAuth = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT jsonAuthData FROM auth";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String json = rs.getString("jsonAuthData");
                        AuthData authData = new Gson().fromJson(json, AuthData.class);
                        allAuth.add(authData);
                    }
                    return allAuth;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
    }

    @Override
    public void delAuth(String authToken) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if (authData == null) {throw new DataAccessException("Error: unauthorized", 401);
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE jsonAuthData = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, new Gson().toJson(authData));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
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
