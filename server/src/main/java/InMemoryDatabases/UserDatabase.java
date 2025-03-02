package InMemoryDatabases;

import dataaccess.UserDAOInterface;
import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class UserDatabase {
    private Map<String, UserData> userMap = new HashMap<>();
    public void addUser(UserData userData) {
        userMap.put(userData.username(), userData);
    }
    public UserData findUser(String username) {
        return userMap.get(username);
    }
}
