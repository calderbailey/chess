import chess.ChessGame;
import dataaccess.DatabaseManager;
import dataaccess.MySqlGameDAO;
import exceptionhandling.DataAccessException;

import java.awt.desktop.SystemSleepEvent;
import java.sql.Connection;
import java.util.ArrayList;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import requestresult.CreateRequest;
import requestresult.CreateResult;
import service.GameService;

public class Test {
    public static void main(String[] args) throws Exception {
        // Call createDatabase() normally
        MySqlGameDAO sgame = new MySqlGameDAO();
        MySqlAuthDAO sauth = new MySqlAuthDAO();
        sauth.createAuth("USER");
        sgame.createGame("HELLO");
        sgame.createGame("HELL3O");
        ArrayList<GameData> gameList = sgame.getGameList();
        System.out.printf("Number of Games: " + String.valueOf(gameList.size()) + "\n\n");
        for (GameData gameData1 : gameList) {
            System.out.printf(String.valueOf(gameData1) + "\n\n");
        }
        sgame.clear();
        gameList = sgame.getGameList();
        System.out.printf("Number of Games: " + String.valueOf(gameList.size()) + "\n\n");
        for (GameData gameData1 : gameList) {
            System.out.printf(String.valueOf(gameData1) + "\n\n");
        }
        MySqlUserDAO suser = new MySqlUserDAO();
        suser.clear();
    }
}
