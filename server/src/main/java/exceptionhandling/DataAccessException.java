package exceptionhandling;

import com.google.gson.Gson;

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

}
