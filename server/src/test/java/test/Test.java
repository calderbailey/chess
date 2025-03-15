package test;

import dataaccess.*;
import exceptionhandling.DataAccessException;
import org.junit.jupiter.api.BeforeEach;

public abstract class Test {
    protected static final UserDAOInterface USER_DAO;
    protected static final AuthDAOInterface AUTH_DAO;
    protected static final GameDAOInterface GAME_DAO;

    static {
        try {
            GAME_DAO = new MySqlGameDAO();
            AUTH_DAO = new MySqlAuthDAO();
            USER_DAO = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    protected void setup() throws DataAccessException{
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
    }
}
