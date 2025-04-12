package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{

    private ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID, String teamColor, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID, teamColor);
        this.move = move;
    }

    public ChessMove getMove() {
        return this.move;
    }

    public void setMove(ChessMove move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return "MakeMoveCommand{" +
                "commandType=" + commandType +
                ", authToken='" + authToken + '\'' +
                ", gameID=" + gameID +
                ", teamColor='" + teamColor + '\'' +
                ", move=" + move +
                '}';
    }
}
