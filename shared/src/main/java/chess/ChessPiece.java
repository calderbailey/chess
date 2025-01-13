package chess;

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
            System.out.print("No Piece at this Location");
        }
        else if (type == PieceType.KING){
            System.out.print("This Piece is a King.");
        }
        else if (type == PieceType.QUEEN){
            System.out.print("This Piece is a Queen.");
        }
        else if (type == PieceType.BISHOP){
            System.out.print("This Piece is a Bishop.");
        }
        else if (type == PieceType.KNIGHT){
            System.out.print("This Piece is a Knight.");
        }
        else if (type == PieceType.ROOK){
            return RookMoves(board, myPosition);
        }
        else if (type == PieceType.PAWN){
            System.out.print("This Piece is a Pawn.");
        }
        else {
            System.out.print("This is an INVALID piece type.");
        }
        return null;
    }

    private Collection<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //INCREASING ROW SCREEN
        int RowIndexInc = myRow+1;
        while (RowIndexInc <= 8) {
            ChessPosition NewPosition = new ChessPosition(RowIndexInc, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                RowIndexInc++;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //DECREASING ROW SCREEN
        int RowIndexDec = myRow-1;
        while (RowIndexDec >= 1) {
            ChessPosition NewPosition = new ChessPosition(RowIndexDec, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                RowIndexDec--;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //INCREASING COL SCREEN
        int ColIndexInc = myCol+1;
        while (ColIndexInc <= 8) {
            ChessPosition NewPosition = new ChessPosition(myRow, ColIndexInc);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                ColIndexInc++;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //DECREASING COL SCREEN
        int ColIndexDec = myCol-1;
        while (ColIndexDec >= 1) {
            ChessPosition NewPosition = new ChessPosition(myRow, ColIndexDec);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                ColIndexDec--;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        return MoveList;
    }
}
