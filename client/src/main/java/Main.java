import chess.*;
import client.ServerFacade;
import exceptionhandling.DataAccessException;
import requestresult.ClearRequest;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
    }
}