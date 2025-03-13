package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAOInterface {
    private static final Map<String, UserData> USER_MAP = new HashMap<>();

    @Override
    public void createUser(UserData userData) {
        USER_MAP.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return USER_MAP.get(username);
    }

    @Override
    public void clear() {
        USER_MAP.clear();
    }
}
