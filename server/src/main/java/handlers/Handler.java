package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.AuthDAOInterface;
import dataaccess.MemoryAuthDAO;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.GameData;
import spark.Request;

public abstract class Handler {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GameData.class, new CustomGameDataSerializer())
            .create();

    protected String toJson(Object object) {
        return gson.toJson(object);
    }

    protected <T> T fromJson(Request json, Class<T> request) throws DataAccessException{
        try {
            return gson.fromJson(json.body(), request);
        } catch (Exception e) {
            throw new DataAccessException("Error: bad request", 400);
        }
    }

    public void checkAuth(String authToken) throws DataAccessException {
        AuthDAOInterface authDAO = new MemoryAuthDAO();
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    protected String getUsername(String authToken) {
        AuthDAOInterface authDAO = new MemoryAuthDAO();
        AuthData authData = authDAO.getAuth(authToken);
        return authData.username();
    }
}
