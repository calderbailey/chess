import chess.ChessGame;
import exceptionhandling.DataAccessException;
import model.GameData;
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
//        new PreLoginRepl(serverUrl).run();
        GamePlayRepl gameRepl = new GamePlayRepl(serverUrl, new GameData(1, "USER", "USER","USER", new ChessGame()));
        gameRepl.printBoard();
    }
}