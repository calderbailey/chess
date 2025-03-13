package model;

import chess.ChessGame;

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
}
