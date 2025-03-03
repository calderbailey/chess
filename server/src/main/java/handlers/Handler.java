package handlers;

import com.google.gson.Gson;
import exceptionhandling.DataAccessException;
import spark.Request;

public abstract class Handler {
    protected final Gson gson = new Gson();

    protected String toJson(Object object) throws DataAccessException{
        return gson.toJson(object);
    }

    protected <T> T fromJson(Request json, Class<T> request) throws DataAccessException{
        try {
            return gson.fromJson(json.body(), request);
        } catch (Exception e) {
            throw new DataAccessException("Error: bad request", 400);
        }
    }
}
