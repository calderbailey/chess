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
    private final String defaultMessage;
    private final Integer statusCode;
    public DataAccessException(String defaultMessage, Integer statusCode) {
        this.defaultMessage = defaultMessage;
        this.statusCode = statusCode;
    }

    public String getDefaultMessage(){
        return defaultMessage;
    }

    public Integer getStatusCode(){
        return statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getDefaultMessage()));
    }

    public static DataAccessException fromJson(InputStream stream) {
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        var status = ((Double)map.get("status")).intValue();
        String message = map.get("message").toString();
        return new DataAccessException(message, status);
    }


}
