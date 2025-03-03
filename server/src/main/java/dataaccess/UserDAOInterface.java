package dataaccess;

import exceptionhandling.DataAccessException;
import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAOInterface {
    void createUser(UserData userData);
    UserData getUser(String username);
    void clear();
}
