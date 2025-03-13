package dataaccess;

import exceptionhandling.*;

import java.sql.SQLException;

public abstract class MySqlDAO {

    protected MySqlDAO() throws DataAccessException {
        configureDatabase();
    }

    protected void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : getCreateStatements()) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }
    }

    protected abstract String[] getCreateStatements();
}
