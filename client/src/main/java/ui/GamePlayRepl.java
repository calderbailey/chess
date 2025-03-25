package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;

public class GamePlayRepl {
    private final ChessClient client;
    private final String serverUrl;
    private boolean proceed = true;
    private GameData gameData;
    private ChessBoard board;
    private static final String LETTERS= " abcdefgh ";

    private static final ChessGame game = new ChessGame();

    public GamePlayRepl(String serverUrl, GameData gameData) {
        this.serverUrl = serverUrl;
        this.gameData = gameData;
        board = gameData.game().getBoard();
        client = new ChessClient(serverUrl);
    }

    public void run(){
        System.out.print("YOU HAVE REACHED THE GAMEPLAYREPL\n");
        System.out.printf(gameData.toString() + "\n\n");
        new PostLoginRepl(serverUrl).run();
    }

    public void printBoard(){
        char[] boardArray = chessboardToCharacterArray();
        String boardString = combinedBoardString(boardArray);
        System.out.printf(boardString);
    }

    private char[] chessboardToCharacterArray(){
        StringBuilder chessString = new StringBuilder();
        chessString.append(LETTERS);
        int row = 1;
        int col = 1;
        while (row <= 8) {
            StringBuilder rowString = new StringBuilder();
            rowString.append(row);
            while (col <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    rowString.append(" ");
                } else {
                    rowString.append(board.getPiece(new ChessPosition(row, col)).toString());
                }
                col ++;
            }
            rowString.append(row);
            chessString.append(rowString);
            row ++;
            col = 1;
        }
        chessString.append(LETTERS);
        return chessString.toString().toCharArray();
    }

    private String combinedBoardString(char[] chessPieces) {
        StringBuilder finalString = new StringBuilder();
        int startIndex = 0;
        int endIndex= 10;
        finalString.append(boarderRowMaker(Arrays.copyOfRange(chessPieces, startIndex, endIndex)));
        startIndex = incrementIndex(startIndex);
        endIndex = incrementIndex(endIndex);
        String rowColor = "WHITE";
        while (endIndex < 99) {
            finalString.append(rowMaker(Arrays.copyOfRange(chessPieces, startIndex, endIndex), rowColor));
            rowColor = rowColorSwitch(rowColor);
            startIndex = incrementIndex(startIndex);
            endIndex = incrementIndex(endIndex);
        }
        finalString.append(boarderRowMaker(Arrays.copyOfRange(chessPieces, startIndex, endIndex)));
        return finalString.toString();
    }

    private int incrementIndex(int index) {
        return index + 10;
    }

    private String rowColorSwitch(String rowColor) {
        if (rowColor.equals("WHITE")) {
            return "BLACK";
        }
        return "WHITE";
    }

    private String boarderRowMaker(char[] rowChars) {
        StringBuilder rowString = new StringBuilder();
        int index = 0;
        while (index <10) {
            rowString.append(boarderSquare(rowChars[index]));
            index ++;
        }
        rowString.append(newLine());
        return rowString.toString();
    }

    private String rowMaker(char[] rowChars, String rowColor) {
        StringBuilder rowString = new StringBuilder();
        int index = 0;
        rowString.append(boarderSquare(rowChars[index]));
        index ++;
        String squareColor = rowColor;
        while (index <=8) {
            switch (squareColor) {
                case "WHITE" -> {
                    rowString.append(whiteSquare(rowChars[index]));
                    squareColor = "BLACK";
                }
                case "BLACK" -> {
                    rowString.append(blackSquare(rowChars[index]));
                    squareColor = "WHITE";
                }
            }
            index ++;
        }
        rowString.append(boarderSquare(rowChars[index]));
        rowString.append(newLine());
        return rowString.toString();
    }

    private String newLine() {
        return EscapeSequences.RESET_BG_COLOR + "\n";
    }

    private String boarderSquare(char piece) {
        return EscapeSequences.SET_BG_COLOR_LIGHT_GREY + spaceBuilder(piece);
    }

    private String whiteSquare(char piece) {
        return EscapeSequences.SET_BG_COLOR_WHITE + spaceBuilder(piece);
    }

    private String blackSquare(char piece) {
        return EscapeSequences.SET_BG_COLOR_BLACK + spaceBuilder(piece);
    }

    private String spaceBuilder(char item) {
        return " " + item + " ";
    }

    private String addBoarder(ArrayList<String> rowStrings) {
        StringBuilder finalString = new StringBuilder();
        finalString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                           LETTERS +
                           EscapeSequences.RESET_BG_COLOR +
                           "\n");
        Integer index = 8;
        for (String row : rowStrings) {
            finalString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                               " " + index.toString() + " " +
                               EscapeSequences.RESET_BG_COLOR +
                               row +
                               EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                               " " + index.toString() + " " +
                               EscapeSequences.RESET_BG_COLOR +
                               "\n");
            index --;
        }
        finalString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                LETTERS +
                EscapeSequences.RESET_BG_COLOR +
                "\n");
        return finalString.toString();
    }
}
