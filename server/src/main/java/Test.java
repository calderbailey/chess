import chess.ChessGame;
import dataaccess.DatabaseManager;
import dataaccess.MySqlGameDAO;
import exceptionhandling.DataAccessException;
import java.sql.Connection;
import dataaccess.*;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import service.GameService;

public class Test {
    public static void main(String[] args) throws Exception {
        // Call createDatabase() normally
        GameService game = new GameService();
        CreateResult res;
        res = game.createGame(new CreateRequest("GAME 1"));
        System.out.printf(String.valueOf(res.gameID()));
    }
}
