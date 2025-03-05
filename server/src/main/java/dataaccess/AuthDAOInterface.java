package dataaccess;

import exceptionhandling.DataAccessException;
import model.AuthData;
import model.UserData;

public interface AuthDAOInterface {
    AuthData createAuth(String username);
    AuthData getAuth(String authToken);
    void delAuth(String authToken) throws DataAccessException;
    void clear();
}
