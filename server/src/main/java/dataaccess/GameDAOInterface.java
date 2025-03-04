package dataaccess;

import model.GameData;
import java.util.ArrayList;

public interface GameDAOInterface {
    Integer createGame(String gameName);
    GameData getGame(Integer gameID);
    void delGame(Integer gameID);
    void clear();
    Integer createGameID();
    ArrayList<GameData> getGameList();
}
