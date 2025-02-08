package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChessPiece)) {
            return false;
        }
        ChessPiece cp = (ChessPiece) obj;
        return (pieceColor == cp.getTeamColor() && type == cp.getPieceType());
    }

    @Override
    public String toString() {
        if (type == PieceType.KNIGHT) {
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                return "N";
            }
            else {
                return "n";
            }
        }
        else {
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                return getPieceType().toString().substring(0, 1).toUpperCase();
            }
            return getPieceType().toString().substring(0, 1).toLowerCase();
        }
    }


    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        PieceType type = piece.getPieceType();
        if (type == null) {
            throw new NullPointerException("No Piece at this Location");
        }
        else if (type == PieceType.KING){
            return calculateKingMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.QUEEN){
            return calculateQueenMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.BISHOP){
            return calculateBishopMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.KNIGHT){
            return calculateKnightMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.ROOK){
            return calculateRookMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.PAWN){
            return calculatePawnMoves(pieceColor,board, myPosition);
        }
        else {
            throw new IllegalArgumentException("INVALID piece type.");
        }
    }

    private Collection<ChessMove> calculateKingMoves(ChessGame.TeamColor pieceColor,
                                                           ChessBoard board,
                                                           ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        int rowIndex = myPosition.getRow()-1;
        int colIndex = myPosition.getColumn() -1;
        int rowMax = myPosition.getRow()+1;
        int colMax = myPosition.getColumn() +1;
        //SCREENING SURROUNDING SQUARES
        while (rowIndex <= rowMax) {
            while (colIndex <= colMax) {
                if (rowIndex != myPosition.getRow() || colIndex != myPosition.getColumn()) {
                    ChessPosition newPosition = new ChessPosition(rowIndex, colIndex);
                    ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                    moveList.add(newMove);
                }
                colIndex++;
            }
            colIndex = myPosition.getColumn() -1;
            rowIndex++;
        }
        return calculateValidMoves(pieceColor, board, moveList);
    }

    private Collection<ChessMove> calculateQueenMoves(ChessGame.TeamColor pieceColor,
                                                            ChessBoard board,
                                                            ChessPosition myPosition) {
        Collection<ChessMove> BishopMoves = calculateBishopMoves(pieceColor, board, myPosition);
        Collection<ChessMove> RookMoves = calculateRookMoves(pieceColor, board, myPosition);
        Collection<ChessMove> QueenMoves = new ArrayList<>();
        QueenMoves.addAll(BishopMoves);
        QueenMoves.addAll(RookMoves);
        return QueenMoves;
    }

    private Collection<ChessMove> calculateBishopMoves(ChessGame.TeamColor pieceColor,
                                                             ChessBoard board,
                                                             ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int rowIndex = myRow+1;
        int colIndex = myCol-1;
        //UPPER LEFT SCREEN
        while (rowIndex <= 8 && colIndex >= 1) {
            ChessPosition newPosition = new ChessPosition(rowIndex, colIndex);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            rowIndex++;
            colIndex--;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        //UPPER RIGHT SCREEN
        rowIndex = myRow+1;
        colIndex = myCol+1;
        while (rowIndex <= 8 && colIndex <= 8) {
            ChessPosition newPosition = new ChessPosition(rowIndex, colIndex);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            rowIndex++;
            colIndex++;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        //LOWER LEFT SCREEN
        rowIndex = myRow-1;
        colIndex = myCol-1;
        while (rowIndex >= 1 && colIndex >= 1) {
            ChessPosition newPosition = new ChessPosition(rowIndex, colIndex);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            rowIndex--;
            colIndex--;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        //LOWER RIGHT SCREEN
        rowIndex = myRow-1;
        colIndex = myCol+1;
        while (rowIndex >= 1 && colIndex <= 8) {
            ChessPosition newPosition = new ChessPosition(rowIndex, colIndex);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            rowIndex--;
            colIndex++;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        return calculateValidMoves(pieceColor, board, moveList);
    }

    public Collection<ChessMove> calculateKnightMoves(ChessGame.TeamColor pieceColor,
                                                             ChessBoard board,
                                                             ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //UP and DOWN Heavy Moves
        int colIndex = myCol +1;
        int rowIndex = myRow +2;
        while (rowIndex >= myRow -2) {
            while (colIndex >= myCol -1) {
                moveList.add(new ChessMove(myPosition,
                        new ChessPosition(rowIndex, colIndex),
                        null));
                colIndex = colIndex -2;
            }
            colIndex = myCol +1;
            rowIndex = rowIndex -4;
        }

        //LEFT and RIGHT Heavy Moves
        colIndex = myCol +2;
        rowIndex = myRow +1;
        while (colIndex >= myCol -2) {
            while (rowIndex >= myRow -1) {
                moveList.add(new ChessMove(myPosition,
                        new ChessPosition(rowIndex, colIndex),
                        null));
                rowIndex = rowIndex -2;
            }
            rowIndex = myRow +1;
            colIndex = colIndex -4;
        }
        return calculateValidMoves(pieceColor, board, moveList);
    }

    private Collection<ChessMove> calculateRookMoves(ChessGame.TeamColor pieceColor,
                                                    ChessBoard board,
                                                    ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int index;
        //INCREASING ROW SCREEN
        index = myRow+1;
        while (index <= 8) {
            ChessPosition newPosition = new ChessPosition(index, myCol);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            index++;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        //DECREASING ROW SCREEN
        index = myRow-1;
        while (index >= 1) {
            ChessPosition newPosition = new ChessPosition(index, myCol);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            index--;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        //INCREASING COL SCREEN
        index = myCol+1;
        while (index <= 8) {
            ChessPosition newPosition = new ChessPosition(myRow, index);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            index++;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        //DECREASING COL SCREEN
        index = myCol-1;
        while (index >= 1) {
            ChessPosition newPosition = new ChessPosition(myRow, index);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            index--;
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
        return calculateValidMoves(pieceColor, board, moveList);
    }

    public Collection<ChessMove> calculatePawnMoves(ChessGame.TeamColor pieceColor,
                                                           ChessBoard board,
                                                           ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int forward;
        int startRow;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            forward = 1;
            startRow = 2;
        }
        else {
            forward = -1;
            startRow = 7;
        }
        ChessPosition newPosition = new ChessPosition(myRow + forward, myCol);
        ChessPiece newPositionPiece = board.getPiece(newPosition);
        if (newPositionPiece == null) {
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moveList.add(newMove);
            if (myRow == startRow) {
                newPosition = new ChessPosition(myRow + forward *2, myCol);
                newPositionPiece = board.getPiece(newPosition);
                if (newPositionPiece == null) {
                    newMove = new ChessMove(myPosition, newPosition, null);
                    moveList.add(newMove);
                }
            }
        }
        if (myCol + 1 <= 8) {
            newPosition = new ChessPosition(myRow + forward, myCol + 1);
            newPositionPiece = board.getPiece(newPosition);
            if (newPositionPiece != null && newPositionPiece.getTeamColor() != pieceColor) {
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveList.add(newMove);
            }
        }
        if (myCol - 1 >= 1) {
            newPosition = new ChessPosition(myRow + forward, myCol - 1);
            newPositionPiece = board.getPiece(newPosition);
            if (newPositionPiece != null && newPositionPiece.getTeamColor() != pieceColor) {
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveList.add(newMove);
            }
        }

        return checkForPromotion(moveList);
    }

    private Collection<ChessMove> calculateValidMoves(ChessGame.TeamColor pieceColor,
                                                      ChessBoard board,
                                                      Collection<ChessMove> moveList) {
        Collection<ChessMove> modifiedMoveList= new ArrayList<>();
        for (ChessMove move : moveList) {
            if (inBounds(move) && spaceAvailable(move, board, pieceColor)) {
                modifiedMoveList.add(move);
            }
        }
        return modifiedMoveList;
    }

    private boolean inBounds(ChessMove move) {
        int row = move.getEndPosition().getRow();
        int col = move.getEndPosition().getColumn();
        if (row >= 1 && row <= 8) {
            if (col >= 1 && col <= 8) {
                return true;
            }
        }
        return false;
    }

    private boolean spaceAvailable(ChessMove move,
                                   ChessBoard board,
                                   ChessGame.TeamColor pieceColor) {
        ChessPiece spaceStatus = board.getPiece(move.getEndPosition());
        if (spaceStatus != null && spaceStatus.getTeamColor() == pieceColor) {
            return false;
        }
        return true;
    }

    private Collection<ChessMove> checkForPromotion(Collection<ChessMove> input) {
        Collection<ChessMove> output = new ArrayList<>();
        output.addAll(input);
        for (ChessMove move : input) {
            if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) {
                output.remove(move);
                for (ChessPiece.PieceType pt : ChessPiece.PieceType.values()) {
                    if (!pt.equals(ChessPiece.PieceType.PAWN) && !pt.equals(ChessPiece.PieceType.KING)) {
                        ChessMove NewMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), pt);
                        output.add(NewMove);
                    }
                }
            }
        }
        return output;
    }

    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clone = (ChessPiece) super.clone();
            PieceType cloneType = clone.getPieceType();
            ChessGame.TeamColor cloneColor = clone.getTeamColor();
            clone = new ChessPiece(cloneColor, cloneType);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

