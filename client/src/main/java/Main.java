import chess.*;
import client.ServerFacade;
import exceptionhandling.DataAccessException;
import requestresult.ClearRequest;
import requestresult.LoginRequest;
import requestresult.RegisterRequest;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        System.out.printf(serverFacade.register(new RegisterRequest("Bailey", "PAssWord", "Bailey@gmail.com")).toString() + "\n");
        System.out.printf(serverFacade.login(new LoginRequest("Bailey", "PAssWord")).toString());
    }
}