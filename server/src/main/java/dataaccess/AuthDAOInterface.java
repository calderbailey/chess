package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAOInterface {
    AuthData createAuth(String username);
    AuthData getAuth(String token);
    void clear();
}
