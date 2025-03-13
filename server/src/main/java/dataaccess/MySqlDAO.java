package dataaccess;

import exceptionhandling.*;

import java.sql.SQLException;

public abstract class MySqlDAO {
    public void configureDatabase() throws DataAccessException, ResponseException{
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : getCreateStatements()) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    protected abstract String[] getCreateStatements();
}
