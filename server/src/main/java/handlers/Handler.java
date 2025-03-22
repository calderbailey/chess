package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import exceptionhandling.DataAccessException;
import model.AuthData;
import model.GameData;
import spark.Request;
import static service.Service.DATABASE_METHOD;

public abstract class Handler {

    private final static AuthDAOInterface AUTH_DAO;

    static {
        if ("MySql".equals(DATABASE_METHOD)) {
            try {
                AUTH_DAO = new MySqlAuthDAO();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            AUTH_DAO = new MemoryAuthDAO();
        }
    }


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
        if (AUTH_DAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    protected String getUsername(String authToken) throws DataAccessException {
        checkAuth(authToken);
        return AUTH_DAO.getAuth(authToken).username();
    }
}
