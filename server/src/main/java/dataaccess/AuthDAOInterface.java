package dataaccess;

import exceptionhandling.DataAccessException;
import model.AuthData;
import model.UserData;

public interface AuthDAOInterface {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void delAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
