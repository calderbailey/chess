package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private GameData game;

    public LoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game =game;
    }

    public GameData getGame() {
        return game;
    }

    public void setGame(GameData game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "LoadGameMessage{" +
                "serverMessageType=" + getServerMessageType() + ", " +
                "game=" + getGame() +
                '}';
    }
}
