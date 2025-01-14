package chess;

import chess.PieceMoveCalculators.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
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
            return KingMoveCalculator.calculateKingMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.QUEEN){
            return QueenMoveCalculator.calculateQueenMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.BISHOP){
            return BishopMoveCalculator.calculateBishopMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.KNIGHT){
            System.out.print("This Piece is a Knight.");
            return null;
        }
        else if (type == PieceType.ROOK){
            return RookMoveCalculator.calculateRookMoves(pieceColor,board, myPosition);
        }
        else if (type == PieceType.PAWN){
            return PawnMoveCalculator.calculatePawnMoves(pieceColor,board, myPosition, getPieceType());
        }
        else {
            throw new IllegalArgumentException("INVALID piece type.");
        }
    }
}
