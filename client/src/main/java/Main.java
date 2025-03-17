import exceptionhandling.DataAccessException;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        PreLoginRepl repl = new PreLoginRepl(serverUrl);
        repl.run();
    }
}