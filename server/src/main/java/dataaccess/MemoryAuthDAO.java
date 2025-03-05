package dataaccess;

import exceptionhandling.DataAccessException;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAOInterface {
    private static final Map<String, AuthData> AUTH_MAP = new HashMap<>();

    @Override
    public AuthData createAuth(String username) {
        AuthData authData =  new AuthData(generateToken(), username);
        AUTH_MAP.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return AUTH_MAP.get(authToken);
    }

    @Override
    public void delAuth(String authToken) throws DataAccessException {
        if (AUTH_MAP.get(authToken) != null) {
            AUTH_MAP.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public void clear() {
        AUTH_MAP.clear();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
