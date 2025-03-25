package exceptionhandling;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private final Integer statusCode;
    public DataAccessException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode(){
        return statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("status", getStatusCode(), "message", getMessage()));
    }

    public static DataAccessException fromJson(InputStream stream) {
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        var status = ((Double)map.get("status")).intValue();
        String message = map.get("message").toString();
        return new DataAccessException(message, status);
    }
}
