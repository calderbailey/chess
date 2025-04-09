package ui;

import chess.ChessBoard;
import chess.ChessGame.*;
import chess.ChessPiece;
import chess.ChessPiece.*;
import chess.ChessPosition;
import exceptionhandling.DataAccessException;
import model.GameData;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GamePlayRepl {
    private final String serverUrl;
    private final GameData gameData;
    private final ChessBoard board;
    private final String teamColor;
    private static final String LETTERS= " abcdefgh ";
    private boolean proceed = true;

    public GamePlayRepl(String serverUrl, GameData gameData, String teamColor) {
        this.serverUrl = serverUrl;
        this.gameData = gameData;
        board = new ChessBoard();
        board.resetBoard();
        this.teamColor = teamColor;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        while (proceed) {
            String[] userInput = parseInput(scanner.nextLine());
            try {
                checkInput(userInput);
                eval(userInput);
            } catch (Throwable e) {
                var msg = e.getMessage();
                System.out.print("\n" + "*** " + SET_TEXT_COLOR_RED + msg + RESET_TEXT_COLOR + " ***" + "\n");
                System.out.print(help());
            }
        }
        System.exit(1);
    }

    private String[] parseInput(String userInput) {
        return userInput.split(" ");
    }

    private void checkInput(String [] arguments) throws IllegalArgumentException{
        int actualArgs = arguments.length;
        int expectedArgs = switch (arguments[0].toLowerCase()) {
            case "redraw", "leave", "resign", "help" -> 1;
            case "makemove", "highlight" -> 2;
            default -> throw new IllegalArgumentException("Unknown command: " + arguments[0]);
        };
        if (actualArgs != expectedArgs) {
            throw new IllegalArgumentException("ERROR: Invalid Number of Arguments --" +
                    "Note: no spaces are allowed within arguments");
        }
    }

    private void eval(String[] userInput) throws DataAccessException {
        switch (userInput[0].toLowerCase()) {
            case "redraw":
                System.out.print("redraw command \n");
                break;
            case "leave":
                System.out.print("leave command \n");
                break;
            case "makemove":
                System.out.print("makeMove command \n");
                break;
            case "resign":
                System.out.print("resign command \n");
                break;
            case "highlight":
                System.out.print("highlight command \n");
                break;
            case "help":
                System.out.printf(help());
                break;
        }
    }


    public void printBoard(){
        char[] boardArray = chessboardToCharacterArray();
        char[] modifiedBoardArray = teamColorModifier(boardArray);
        String boardString = combinedBoardString(modifiedBoardArray);
        System.out.printf(boardString);
    }

    private char[] chessboardToCharacterArray(){
        StringBuilder chessString = new StringBuilder();
        chessString.append(LETTERS);
        int row = 1;
        int col = 1;
        while (row <= 8) {
            StringBuilder rowString = new StringBuilder();
            rowString.append(9-row);
            while (col <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    rowString.append(" ");
                } else {
                    rowString.append(pieceToSymbol(board.getPiece(new ChessPosition(row, col))));
                }
                col ++;
            }
            rowString.append(9-row);
            chessString.append(rowString);
            row ++;
            col = 1;
        }
        chessString.append(LETTERS);
        return chessString.toString().toCharArray();
    }

    private char[] teamColorModifier(char[] chessArray) {
        if (chessArray == null || chessArray.length <= 1) {
            return chessArray;
        }
        if (teamColor.equalsIgnoreCase("BLACK")) {
            char[] reversedArray = chessArray.clone();
            int left = 0, right = reversedArray.length - 1;
            while (left < right) {
                char temp = reversedArray[left];
                reversedArray[left] = reversedArray[right];
                reversedArray[right] = temp;
                left++;
                right--;
            }
            return reversedArray;
        } else {
            return chessArray;
        }
    }


    private String pieceToSymbol(ChessPiece piece) {
        PieceType type = piece.getPieceType();
        TeamColor color = piece.getTeamColor();

        if (color.equals(TeamColor.WHITE)) {
            switch (type) {
                case KING: return EscapeSequences.WHITE_KING;
                case QUEEN: return EscapeSequences.WHITE_QUEEN;
                case ROOK: return EscapeSequences.WHITE_ROOK;
                case BISHOP: return EscapeSequences.WHITE_BISHOP;
                case KNIGHT: return EscapeSequences.WHITE_KNIGHT;
                case PAWN: return EscapeSequences.WHITE_PAWN;
                default: return "?";
            }
        } else {
            switch (type) {
                case KING: return EscapeSequences.BLACK_KING;
                case QUEEN: return EscapeSequences.BLACK_QUEEN;
                case ROOK: return EscapeSequences.BLACK_ROOK;
                case BISHOP: return EscapeSequences.BLACK_BISHOP;
                case KNIGHT: return EscapeSequences.BLACK_KNIGHT;
                case PAWN: return EscapeSequences.BLACK_PAWN;
                default: return "?";
            }
        }
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
        return EscapeSequences.SET_BG_COLOR_MEDIUM_GREY + spaceBuilder(piece);
    }

    private String blackSquare(char piece) {
        return EscapeSequences.SET_BG_COLOR_BLACK + spaceBuilder(piece);
    }

    private String spaceBuilder(char item) {
        return " " + item + " ";
    }

    private String help() {
        return (SET_TEXT_COLOR_BLUE + "redraw <NAME> " +
                SET_TEXT_COLOR_MAGENTA + "- chess board\n" +
                SET_TEXT_COLOR_BLUE + "leave " +
                SET_TEXT_COLOR_MAGENTA + "- game\n" +
                SET_TEXT_COLOR_BLUE + "makeMove <ChessMove>\n" +
                SET_TEXT_COLOR_BLUE + "resign " +
                SET_TEXT_COLOR_MAGENTA + "- from game\n" +
                SET_TEXT_COLOR_BLUE + "highlight <ChessPiece> " +
                SET_TEXT_COLOR_MAGENTA + "- legal moves\n" +
                SET_TEXT_COLOR_BLUE + "help " +
                SET_TEXT_COLOR_MAGENTA + "- with possible commands\n" +
                RESET_TEXT_COLOR);
    }
}
