import exceptionhandling.DataAccessException;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var serverUrl = "http://localhost:8081";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new PreLoginRepl(serverUrl).run();
    }
}