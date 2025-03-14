package dataaccess;

import exceptionhandling.DataAccessException;
import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAOInterface {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
