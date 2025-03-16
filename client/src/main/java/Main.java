import chess.*;
import client.ServerFacade;
import exceptionhandling.DataAccessException;
import requestresult.*;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        serverFacade.delete(new ClearRequest());
        RegisterResult register = serverFacade.register(new RegisterRequest("Bailey", "PAssWord", "Bailey@gmail.com"));
        String regToken = register.authToken();
        CreateRequest createRequest = new CreateRequest("NEW GAME");
        System.out.printf(serverFacade.createGame(createRequest, regToken).toString() + "\n");
        System.out.printf(serverFacade.logout(new LogoutRequest(null), regToken).toString());
    }
}