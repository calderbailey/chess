import exceptionhandling.DataAccessException;
import requestresult.RegisterRequest;
import ui.PreLoginRepl;
import ui.ServerFacade;
import ui.*;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new PreLoginRepl(serverUrl).run();
    }
}