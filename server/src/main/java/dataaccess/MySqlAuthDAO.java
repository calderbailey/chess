package dataaccess;

import exceptionhandling.DataAccessException;
import model.AuthData;

public class MySqlAuthDAO extends MySqlDAO implements AuthDAOInterface {

    public MySqlAuthDAO() throws DataAccessException {
    }

    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void delAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {
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

}
