package ui;
import chess.ChessBoard;
import chess.ChessGame.*;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPiece.*;
import chess.ChessPosition;
import exceptionhandling.DataAccessException;
import model.GameData;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.*;
import java.util.*;
import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class GamePlayRepl implements NotificationHandler {
    private final String serverUrl;
    private ChessBoard board;
    private final String[] userInput;
    private static final String LETTERS= " abcdefgh ";
    private static final Map<Character, Integer> COLUMN_MAP;
    private static final Map<Character, PieceType> TYPE_MAP;
    private boolean proceed = true;
    private final String playerStatus;
    private GameData gameData;
    private final ChessClient client;
    private String teamColor;
    private WebSocketFacade ws;

    public GamePlayRepl(String serverUrl, String[] userInput, String playerStatus, WebSocketFacade ws) {
        this.serverUrl = serverUrl;
        board = new ChessBoard();
        board.resetBoard();
        this.userInput = userInput;
        this.playerStatus = playerStatus;
        this.client = new ChessClient(serverUrl, this);
        if (playerStatus.equals("Playing")) {
            this.teamColor = userInput[2];
        } else {
            this.teamColor = "WHITE";
        }
        this.ws = ws;
    }

    static {
        COLUMN_MAP = Map.of(
                'a', 1, 'b', 2, 'c', 3, 'd', 4,
                'e', 5, 'f', 6, 'g', 7, 'h', 8);
        TYPE_MAP = new HashMap<>();
        TYPE_MAP.put('r', PieceType.ROOK);
        TYPE_MAP.put('n', PieceType.KNIGHT);
        TYPE_MAP.put('b', PieceType.BISHOP);
        TYPE_MAP.put('q', PieceType.QUEEN);
    }

    public void setWS(WebSocketFacade ws) {this.ws = ws;}

    public void initiate() throws DataAccessException {
        if (playerStatus.equals("Playing")) {
            client.join(userInput);
        } else {
            client.observe(userInput);
        }
        run();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        while (proceed) {
            printPrompt();
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
        new PostLoginRepl(serverUrl).run();
        System.exit(1);
    }

    private String[] parseInput(String userInput) {
        return userInput.split(" ");
    }

    private void checkInput(String [] arguments) throws IllegalArgumentException{
        int actualArgs = arguments.length;
        int expectedArgs = switch (arguments[0].toLowerCase()) {
            case "redraw", "leave", "resign", "help" -> 1;
            case "highlight" -> 3;
            case "makemove" -> 5;
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
                redraw();
                break;
            case "leave":
                leave();
                break;
            case "makemove":
                if (playerStatus.equals("Observing")) {
                    throw new DataAccessException("ERROR: you cannot make a move as an observer", 500);
                }
                makeMove(userInput);
                break;
            case "resign":
                if (playerStatus.equals("Observing")) {
                    throw new DataAccessException("ERROR: you cannot resign as an observer", 500);
                }
                resign();
                break;
            case "highlight":
                highlight(userInput);
                break;
            case "help":
                System.out.printf(help());
                break;
        }
    }

    private void resign() throws DataAccessException {
        ws.resign();
    }

    private void leave() throws DataAccessException {
        try {
            ws.leave();
            proceed = false;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
    }

    private void makeMove(String[] userInput) throws DataAccessException {
        if (gameData.game().isGameComplete()) {
            System.out.printf("\n" + SET_TEXT_COLOR_RED + ">>>  " + "Game Completed" + "  <<<" + RESET_TEXT_COLOR + "\n");
        } else {
            checkInputMakeMove(userInput);
            int startRow = Integer.parseInt(userInput[1]);
            char startCol = userInput[2].charAt(0);
            int endRow = Integer.parseInt(userInput[3]);
            char endCol = userInput[4].charAt(0);
            ChessPosition startPosition = createChessPosition(startRow, startCol);
            ChessPosition endPosition = createChessPosition(endRow, endCol);
            if (startPosition.equals(endPosition)) {
                throw new DataAccessException("ERROR: invalid move", 500);
            }
            PieceType promoPiece = checkPromotion(startPosition, endPosition);
            ChessMove move = new ChessMove(startPosition, endPosition, promoPiece);
            ws.makeMove(move);
        }
    }

    private PieceType checkPromotion(ChessPosition startPosition, ChessPosition endPosition) throws DataAccessException {
        PieceType pieceType = board.getPiece(startPosition).getPieceType();
        if (pieceType.equals(PieceType.PAWN)) {
            if ((teamColor.equals(WHITE) && endPosition.getRow() == 1) || (teamColor.equals(BLACK) && endPosition.getRow() == 8)) {
                System.out.printf("Congratulations your pawn can be promoted!\n" +
                        "Please enter your selected promotion piece <R, N, B, Q>\n" +
                        ">>> ");
                Scanner scanner = new Scanner(System.in);
                String[] userInput = parseInput(scanner.nextLine());
                char promoPiece = userInput[0].toLowerCase().charAt(0);
                checkPromoInput(userInput);
                PieceType promoPieceType = TYPE_MAP.get(promoPiece);
                return promoPieceType;
            }
        }
        return null;
    }

    private void checkPromoInput(String[] userInput) throws DataAccessException {
        char type = userInput[0].toLowerCase().charAt(0);
        List<Character> typeList = List.of('r', 'n', 'b', 'q');
        if (!typeList.contains(type)) {
            throw new DataAccessException("ERROR: invalid promotion type", 500);
        }
    }

    private void checkInputMakeMove(String[] fullUserInput) throws DataAccessException {
        String[] userInput = Arrays.copyOfRange(fullUserInput, 1,5);
        for (String arg : userInput) {
            if (arg.length() != 1) {
                throw new DataAccessException("ERROR: invalid input", 500);
            }
        }
        if (!LETTERS.contains(userInput[1]) || !LETTERS.contains(userInput[3])) {
            throw new DataAccessException("ERROR: valid column values are a-h", 500);
        }
        int startRow = Integer.parseInt(userInput[0]);
        int endRow = Integer.parseInt(userInput[2]);
        if (!(startRow >= 1 && startRow <= 8 && endRow >= 1 && endRow <= 8)) {
            throw new DataAccessException("ERROR: valid row values are 1-8", 500);
        }
    }

    private ChessPosition createChessPosition(int row, char col) {
        int colInt = COLUMN_MAP.get(col);
        return new ChessPosition(row, colInt);
    }

    private void redraw() {
        printBoard(null);
        if (gameData.game().isGameComplete()) {
            System.out.printf(SET_TEXT_COLOR_RED + ">>>  " + "Game Complete" + "  <<<" + RESET_TEXT_COLOR + "\n\n");
        } else {
            System.out.printf(SET_TEXT_COLOR_RED + ">>>  " + "It's the " +
                    gameData.game().getTeamTurn().toString().toLowerCase() + " team's turn" + "  <<<" +
                    RESET_TEXT_COLOR + "\n\n");
        }
    }

    private void highlight(String[] userInput) throws DataAccessException {
        highlightArgChecker(userInput);
        int row = Integer.parseInt(userInput[1]);
        char col = userInput[2].charAt(0);
        ChessPosition position = createChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);
        if (piece != null) {
            Collection <ChessMove> possibleMoves = piece.pieceMoves(board, position);
            Set<ChessPosition> highlightSquares = new LinkedHashSet<>();
            highlightSquares.add(position);
            for (ChessMove move : possibleMoves) {
                highlightSquares.add(move.getEndPosition());
            }
            printBoard(highlightSquares);
        } else {
            throw new DataAccessException("ERROR: no piece found at position (" + userInput[1] + ", " + userInput[2] + ")", 405);
        }
    }

    private void highlightArgChecker(String[] userInput) throws DataAccessException {
        int row;
        try {
            row = Integer.parseInt(userInput[1]);
            if (row > 8 | row < 1) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: row value must be an integer between 1 and 8", 405);
        }
        char col;
        try {
            col = userInput[2].charAt(0);
            if (!COLUMN_MAP.containsKey(col)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: col value must be a letter between a and h", 405);
        }
    }

    private void printBoard(Set<ChessPosition> highlightSquares){
        char[] boardArray = chessboardToCharacterArray();
        char[] modifiedBoardArray = teamColorModifier(boardArray);
        String boardString = combinedBoardString(modifiedBoardArray, highlightSquares);
        System.out.printf( "\n" + boardString + "\n");
    }

    private char[] chessboardToCharacterArray(){
        StringBuilder chessString = new StringBuilder();
        chessString.append(LETTERS);
        int row = 8;
        int col = 8;
        while (row >= 1) {
            StringBuilder rowString = new StringBuilder();
            rowString.append(row);
            while (col >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    rowString.append(" ");
                } else {
                    rowString.append(pieceToSymbol(board.getPiece(new ChessPosition(row, col))));
                }
                col --;
            }
            rowString.append(row);
            chessString.append(rowString);
            row --;
            col = 8;
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

        if (color.equals(WHITE)) {
            switch (type) {
                case KING: return WHITE_KING;
                case QUEEN: return WHITE_QUEEN;
                case ROOK: return WHITE_ROOK;
                case BISHOP: return WHITE_BISHOP;
                case KNIGHT: return WHITE_KNIGHT;
                case PAWN: return WHITE_PAWN;
                default: return "?";
            }
        } else {
            switch (type) {
                case KING: return BLACK_KING;
                case QUEEN: return BLACK_QUEEN;
                case ROOK: return BLACK_ROOK;
                case BISHOP: return BLACK_BISHOP;
                case KNIGHT: return BLACK_KNIGHT;
                case PAWN: return BLACK_PAWN;
                default: return "?";
            }
        }
    }

    private String combinedBoardString(char[] chessPieces, Set<ChessPosition> highlightSquares) {
        StringBuilder finalString = new StringBuilder();
        int startIndex = 0;
        int endIndex= 10;
        finalString.append(boarderRowMaker(Arrays.copyOfRange(chessPieces, startIndex, endIndex)));
        startIndex = incrementIndex(startIndex);
        endIndex = incrementIndex(endIndex);
        String rowColor = "WHITE";
        int row = 8;
        while (endIndex < 99) {
            finalString.append(rowMaker(Arrays.copyOfRange(chessPieces, startIndex, endIndex), row, rowColor, highlightSquares));
            rowColor = rowColorSwitch(rowColor);
            startIndex = incrementIndex(startIndex);
            endIndex = incrementIndex(endIndex);
            row --;
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
        while (index <= 9) {
            rowString.append(boarderSquare(rowChars[index]));
            index ++;
        }
        rowString.append(newLine());
        return rowString.toString();
    }

    private String rowMaker(char[] rowChars, Integer row, String rowColor, Set<ChessPosition> highlightSquares) {
        StringBuilder rowString = new StringBuilder();
        int index = 9;
        rowString.append(boarderSquare(rowChars[index]));
        index --;
        String squareColor = rowColor;
        ChessPosition pos;
        while (index >=1) {
            int actualRow = teamColor.equalsIgnoreCase("WHITE") ? row : 9 - row;
            int actualCol = teamColor.equalsIgnoreCase("BLACK") ? index : 9 - index;
            pos = new ChessPosition(actualRow, actualCol);
            if (highlightSquares != null && highlightSquares.contains(pos)) {
                ChessPosition initialPos = highlightSquares.iterator().next();
                if (pos.equals(initialPos)) {
                    rowString.append(initialSquare(rowChars[index], squareColor));
                    squareColor = squareColorSwap(squareColor);
                } else {
                    rowString.append(highlightedSquare(rowChars[index], squareColor));
                    squareColor = squareColorSwap(squareColor);
                }
            } else if (squareColor.equals("WHITE")) {
                rowString.append(whiteSquare(rowChars[index]));
                squareColor = squareColorSwap(squareColor);
            } else {
                rowString.append(blackSquare(rowChars[index]));
                squareColor = squareColorSwap(squareColor);
            }
            index --;
        }
        rowString.append(boarderSquare(rowChars[index]));
        rowString.append(newLine());
        return rowString.toString();
    }

    private String squareColorSwap(String squareColor) {
        if (squareColor.equals("WHITE")) {
            return "BLACK";
        } else {
            return "WHITE";
        }
    }

    private String newLine() {
        return RESET_BG_COLOR + "\n";
    }

    private String boarderSquare(char piece) {
        return SET_BG_COLOR_LIGHT_GREY + spaceBuilder(piece);
    }

    private String whiteSquare(char piece) {
        return SET_BG_COLOR_MEDIUM_GREY + spaceBuilder(piece);
    }

    private String blackSquare(char piece) {
        return SET_BG_COLOR_BLACK + spaceBuilder(piece);
    }

    private String highlightedSquare(char piece, String squareColor) {
        if (squareColor.equals("WHITE")) {
            return SET_BG_COLOR_LIGHT_BLUE + spaceBuilder(piece);
        } else {
            return SET_BG_COLOR_BLUE + spaceBuilder(piece);
        }
    }

    private String initialSquare(char piece, String squareColor) {
        return SET_BG_COLOR_DARK_GREEN + spaceBuilder(piece);
    }

    private String spaceBuilder(char item) {
        return " " + item + " ";
    }

    private String help() {
        return (SET_TEXT_COLOR_BLUE + "redraw " + SET_TEXT_COLOR_MAGENTA + "- chess board\n" +
                SET_TEXT_COLOR_BLUE + "leave " + SET_TEXT_COLOR_MAGENTA + "- game\n" +
                SET_TEXT_COLOR_BLUE + "makeMove <StartRow> <StartColumn> <EndRow> <EndColumn> \n" +
                SET_TEXT_COLOR_BLUE + "resign " + SET_TEXT_COLOR_MAGENTA + "- from game\n" +
                SET_TEXT_COLOR_BLUE + "highlight <Row> <Col> " + SET_TEXT_COLOR_MAGENTA + "- legal moves\n" +
                SET_TEXT_COLOR_BLUE + "help " + SET_TEXT_COLOR_MAGENTA + "- with possible commands\n" +
                RESET_TEXT_COLOR);
    }

    private void printPrompt() {
        if (Objects.equals(playerStatus, "Playing")) {
            System.out.print("[Playing] >>> ");
        } else {
            System.out.print("[Observing] >>> ");
        }
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> {
                gameData = ((LoadGameMessage) message).getGame();
                board = gameData.game().getBoard();
                redraw();
                printPrompt();
            }
            case NOTIFICATION -> {
                String notice = ((NotificationMessage) message).getMessage();
                System.out.printf("\n" + SET_TEXT_COLOR_RED + ">>>  " + notice + "  <<<" + RESET_TEXT_COLOR + "\n");
                printPrompt();
            }
            case ERROR -> {
                String errorMessage = ((ErrorMessage) message).getErrorMessage();
                System.out.printf("\n" + SET_TEXT_COLOR_RED + ">>>  " + errorMessage + "  <<<" + RESET_TEXT_COLOR + "\n");
            }
        }
    }
}
