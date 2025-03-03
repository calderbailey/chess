package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAOInterface;
import dataaccess.MemoryAuthDAO;
import exceptionhandling.DataAccessException;
import spark.Request;

public abstract class Handler {
    protected final Gson gson = new Gson();

    protected String toJson(Object object) {
        return gson.toJson(object);
    }

    protected <T> T fromJson(Request json, Class<T> request) throws DataAccessException{
        try {
            return gson.fromJson(json.body(), request);
        } catch (Exception e) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    protected void checkAuth(String authToken) throws DataAccessException {
        AuthDAOInterface authDAO = new MemoryAuthDAO();
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }
}
