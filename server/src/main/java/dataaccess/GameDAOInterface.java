package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public interface GameDAOInterface {
    Integer createGame(String gameName);
    GameData getGame(Integer gameID);
    void delGame(Integer gameID);
    void clear();
    Integer createGameID();
}
