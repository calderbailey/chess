package dataaccess;

import com.google.gson.Gson;
import exceptionhandling.DataAccessException;
import model.UserData;

public class MySqlUserDAO extends MySqlDAO implements UserDAOInterface{

    public MySqlUserDAO() throws DataAccessException {
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        String updateStatement = "INSERT INTO user (username, jsonUserData) VALUES (?, ?)";
        String json = new Gson().toJson(userDataEncrypter(userData));
        executeUpdate(updateStatement, userData.username(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT jsonUserData FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("jsonUserData");
                        return new Gson().fromJson(json, UserData.class);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        clearHelper("user");
    }

    @Override
    protected String[] getCreateStatements() {
        return new String[]{
                """
            CREATE TABLE IF NOT EXISTS user (
              `id` INT NOT NULL AUTO_INCREMENT,
              `username` VARCHAR(255) NOT NULL,
              `jsonUserData` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
    }
}
