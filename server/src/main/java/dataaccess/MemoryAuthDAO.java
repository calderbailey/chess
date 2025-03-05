package dataaccess;

import exceptionhandling.DataAccessException;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAOInterface {
    private static final Map<String, AuthData> authMap = new HashMap<>();

    @Override
    public AuthData createAuth(String username) {
        AuthData authData =  new AuthData(generateToken(), username);
        authMap.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    @Override
    public void delAuth(String authToken) throws DataAccessException {
        if (authMap.get(authToken) != null) {
            authMap.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public void clear() {
        authMap.clear();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
