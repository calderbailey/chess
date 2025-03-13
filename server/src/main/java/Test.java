import chess.ChessGame;
import dataaccess.DatabaseManager;
import dataaccess.MySqlGameDAO;
import exceptionhandling.DataAccessException;
import java.sql.Connection;
import dataaccess.*;
import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import service.GameService;

public class Test {
    public static void main(String[] args) throws Exception {
        // Call createDatabase() normally
        MySqlGameDAO sgame = new MySqlGameDAO();
        Integer gameID = sgame.createGame("GAME 1");
        GameData gameData = sgame.getGame(gameID);
        System.out.printf(gameData.toString());
    }
}
