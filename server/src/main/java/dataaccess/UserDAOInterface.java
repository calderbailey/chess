package dataaccess;

import exceptionhandling.DataAccessException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public interface UserDAOInterface {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;

    default UserData userDataEncrypter(UserData userData) {
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        return new UserData(userData.username(), hashedPassword, userData.email());
    }
}
