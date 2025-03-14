package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(Integer gameID,
                       String whiteUsername,
                       String blackUsername,
                       String gameName,
                       ChessGame game) {
    @Override
    public String toString() {
        return ("Game ID: " + gameID +
                "\nWhite Username: " + whiteUsername +
                "\nBlack Username: " + blackUsername +
                "\nGame Name: " + gameName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        return Objects.equals(gameID(), gameData.gameID()) &&
                Objects.equals(whiteUsername(), gameData.whiteUsername()) &&
                Objects.equals(blackUsername(), gameData.blackUsername()) &&
                Objects.equals(gameName(), gameData.gameName()) &&
                Objects.equals(game(), gameData.game());
    }


}
