package dataaccess;

import model.UserData;

public interface UserDAOInterface {
    void createUser(UserData userData);
    UserData getUser(String username);
    void clear();
}
