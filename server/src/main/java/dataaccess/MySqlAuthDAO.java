package dataaccess;

import exceptionhandling.DataAccessException;
import model.AuthData;

public class MySqlAuthDAO implements AuthDAOInterface{
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
}
