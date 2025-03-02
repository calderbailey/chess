package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAOInterface {
    private Map<String, UserData> userMap = new HashMap<>();

    @Override
    public void createUser(UserData userData) {
        userMap.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return userMap.get(username);
    }

    @Override
    public void clear() {

    }
}
