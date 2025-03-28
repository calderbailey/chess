package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessBoard implements Cloneable{
    private ChessPiece[][] squares = new ChessPiece[9][9];
    public ChessBoard() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    private ChessPiece[][] getArray() {
        return this.squares;
    }

    private void setArray(ChessPiece[][] newArray) {
        this.squares = newArray;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        ChessPiece piece = squares[position.getRow()][position.getColumn()];
        return piece;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Clear Existing Board
        squares = new ChessPiece[9][9];
        //Setup Order
        Collection<ChessPiece.PieceType> pieceList = new ArrayList();
        pieceList.add(ChessPiece.PieceType.ROOK);
        pieceList.add(ChessPiece.PieceType.KNIGHT);
        pieceList.add(ChessPiece.PieceType.BISHOP);
        pieceList.add(ChessPiece.PieceType.QUEEN);
        pieceList.add(ChessPiece.PieceType.KING);
        pieceList.add(ChessPiece.PieceType.BISHOP);
        pieceList.add(ChessPiece.PieceType.KNIGHT);
        pieceList.add(ChessPiece.PieceType.ROOK);

        //White
        int rowIndex = 1;
        int colIndex = 1;
        for (ChessPiece.PieceType type: pieceList) {
            ChessPosition position = new ChessPosition(rowIndex, colIndex);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, type);
            addPiece(position, piece);
            colIndex ++;
        }
        rowIndex = 2;
        colIndex = 1;
        while (colIndex <= 8) {
            ChessPosition position = new ChessPosition(rowIndex, colIndex);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
            colIndex ++;
        }

        //Black
        rowIndex = 8;
        colIndex = 1;
        for (ChessPiece.PieceType type: pieceList) {
            ChessPosition position = new ChessPosition(rowIndex, colIndex);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, type);
            addPiece(position, piece);
            colIndex ++;
        }
        rowIndex = 7;
        colIndex = 1;
        while (colIndex <= 8) {
            ChessPosition position = new ChessPosition(rowIndex, colIndex);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
            colIndex ++;
        }
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            ChessPiece[][] newBoard = new ChessPiece[9][9];
            for (int rowIndex = 1; rowIndex <= 8; rowIndex ++) {
                for (int colIndex = 1; colIndex <= 8; colIndex ++) {
                    ChessPiece clonedPiece = clone.getArray()[rowIndex][colIndex];
                    if (clonedPiece == null) {
                        newBoard[rowIndex][colIndex] = null;
                    } else {
                        newBoard[rowIndex][colIndex] = clonedPiece.clone();
                    }
                }
            }
            clone.setArray(newBoard);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
